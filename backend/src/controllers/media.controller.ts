import { Request, Response } from "express";
import { Media } from "../models/media.model";
import { searchAudius } from "../services/media/audius.service";

export const searchMedia = async (
    req: Request,
    res: Response
) => {

    try {

        const query = String(req.query.q || "")
            .trim()
            .toLowerCase();

        if (!query) {
            return res.status(400).json({
                success: false,
                message: "Query is required"
            });
        }

        const words = query
            .split(/\s+/)
            .filter(Boolean);

        const regex = new RegExp(
            words.join("|"),
            "i"
        );

        const results = await Media.find({
            $or: [
                { title: regex },
                { artist: regex },
                { album: regex }
            ]
        }).lean();

        if (results.length === 0) {

            try {

                const externalResults =
                    await searchAudius(query);

                if (externalResults.length === 0) {
                    return res.status(404).json({
                        success: false,
                        message: "Media not found"
                    });
                }

                return res.json({
                    success: true,
                    media: externalResults[0],
                    results: externalResults
                });

            } catch (error) {

                console.error(
                    "External media search failed:",
                    error
                );

                return res.status(502).json({
                    success: false,
                    message: "Music search unavailable"
                });
            }
        }

        const ranked = results
            .map(media => {

                const title =
                    media.title.toLowerCase();

                const artist =
                    media.artist.toLowerCase();

                const album =
                    media.album?.toLowerCase() || "";

                let score = 0;

                // Highest priority: exact title
                if (title === query) {
                    score += 100;
                }

                // Title contains complete query
                else if (title.includes(query)) {
                    score += 70;
                }

                // Individual query words in title
                for (const word of words) {
                    if (title.includes(word)) {
                        score += 30;
                    }
                }

                // Artist match
                if (artist === query) {
                    score += 60;
                } else if (artist.includes(query)) {
                    score += 40;
                }

                // Album match
                if (album === query) {
                    score += 50;
                } else if (album.includes(query)) {
                    score += 30;
                }

                return {
                    media,
                    score
                };
            })
            .sort(
                (a, b) =>
                    b.score - a.score
            );

        const bestResult = ranked[0];

        if (!bestResult) {
            return res.status(404).json({
                success: false,
                message: "Media not found"
            });
        }

        const bestMatch = bestResult.media;

        return res.json({
            success: true,

            media: {
                title: bestMatch.title,
                artist: bestMatch.artist,
                album: bestMatch.album,
                duration: bestMatch.duration,
                uri: bestMatch.uri,
                coverArt: bestMatch.coverArt
            },

            results: ranked
                .slice(0, 10)
                .map(item => ({
                    title: item.media.title,
                    artist: item.media.artist,
                    album: item.media.album,
                    duration: item.media.duration,
                    uri: item.media.uri,
                    coverArt: item.media.coverArt,
                    score: item.score
                }))
        });

    } catch (error) {

        console.error(
            "Media search failed:",
            error
        );

        return res.status(500).json({
            success: false,
            message: "Media search failed"
        });
    }
};

export const streamMedia = async (
    req: Request,
    res: Response
) => {

    try {

        const trackId =
            String(req.params.trackId || "")
                .trim();

        if (!trackId) {
            return res.status(400).json({
                success: false,
                message: "Track ID is required"
            });
        }

        const audiusUrl =
            `https://api.audius.co/v1/tracks/${encodeURIComponent(trackId)}/stream`;

        console.log(
            `[AUDIUS STREAM] Proxying track: ${trackId}`
        );

        const response =
            await fetch(
                audiusUrl,
                {
                    method: "GET",
                    redirect: "follow",
                    headers: {
                        "User-Agent":
                            "Mozilla/5.0",
                        "Accept":
                            "*/*"
                    }
                }
            );

        console.log(
            `[AUDIUS STREAM] Response: ${response.status}`
        );

        if (!response.ok) {

            const errorText =
                await response.text();

            console.error(
                `[AUDIUS STREAM] Failed: ` +
                `${response.status} ` +
                errorText
            );

            return res.status(
                response.status
            ).json({
                success: false,
                message:
                    "Unable to stream media"
            });
        }

        if (!response.body) {

            return res.status(502).json({
                success: false,
                message:
                    "Audius returned empty stream"
            });
        }


        // Forward content type
        const contentType =
            response.headers.get(
                "content-type"
            );

        if (contentType) {

            res.setHeader(
                "Content-Type",
                contentType
            );
        }
        else {

            res.setHeader(
                "Content-Type",
                "audio/mpeg"
            );
        }


        // Forward content length
        const contentLength =
            response.headers.get(
                "content-length"
            );

        if (contentLength) {

            res.setHeader(
                "Content-Length",
                contentLength
            );
        }


        // Forward range support
        const acceptRanges =
            response.headers.get(
                "accept-ranges"
            );

        if (acceptRanges) {

            res.setHeader(
                "Accept-Ranges",
                acceptRanges
            );
        }


        // Stream body
        const reader =
            response.body.getReader();

        try {

            while (true) {

                const {
                    done,
                    value
                } =
                    await reader.read();

                if (done) {
                    break;
                }

                if (value) {

                    res.write(
                        Buffer.from(value)
                    );
                }
            }

        }
        finally {

            reader.releaseLock();
        }

        res.end();

    }
    catch (error) {

        console.error(
            "[AUDIUS STREAM] Error:",
            error
        );

        if (
            !res.headersSent
        ) {

            return res.status(500).json({
                success: false,
                message:
                    "Media streaming failed"
            });
        }

        res.end();
    }
};