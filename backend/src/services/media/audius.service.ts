export interface AudiusTrack {
    id: string;
    title: string;
    artist: string;
    album?: string;
    duration?: number;
    uri: string;
    coverArt?: string;
}

interface AudiusApiTrack {
    id: string;
    title: string;
    duration?: number;

    artwork?: {
        "150x150"?: string;
        "480x480"?: string;
    };

    user?: {
        name?: string;
    };
}

interface AudiusSearchResponse {
    data?: AudiusApiTrack[];
}

const AUDIUS_API = "https://api.audius.co";

const searchAudiusRaw = async (
    query: string
): Promise<AudiusApiTrack[]> => {

    const encoded = encodeURIComponent(
        query.trim()
    );

    const url =
        `${AUDIUS_API}/v1/tracks/search` +
        `?query=${encoded}&limit=20`;

    console.log(`[AUDIUS] Searching: ${query}`);

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(
            `Audius search failed: ${response.status}`
        );
    }

    const json =
        await response.json() as AudiusSearchResponse;

    const tracks = json.data ?? [];

    console.log(
        `[AUDIUS] Found ${tracks.length} tracks`
    );

    return tracks;
};


/*
|--------------------------------------------------------------------------
| Check whether a track is actually streamable
|--------------------------------------------------------------------------
*/

const isTrackStreamable = async (
    trackId: string
): Promise<boolean> => {

    try {

        const url =
            `${AUDIUS_API}/v1/tracks/${encodeURIComponent(trackId)}/stream?app_name=Synapse`;

        const response = await fetch(url, {
            method: "GET",
            redirect: "manual"
        });

        /*
         * 200 = playable
         * 3xx = Audius has a stream redirect
         *
         * Since fetch with redirect: manual can return 3xx,
         * treat 200-399 as potentially playable.
         */

        if (
            response.status >= 200 &&
            response.status < 400
        ) {
            return true;
        }

        console.log(
            `[AUDIUS] NOT STREAMABLE: ${trackId} | ${response.status}`
        );

        return false;

    } catch (error) {

        console.error(
            `[AUDIUS] STREAM CHECK FAILED: ${trackId}`,
            error
        );

        return false;
    }
};


/*
|--------------------------------------------------------------------------
| Convert Audius track
|--------------------------------------------------------------------------
*/

const convertTrack = (
    track: AudiusApiTrack
): AudiusTrack | null => {

    if (!track.id || !track.title) {
        return null;
    }

    const artist =
        track.user?.name ||
        "Unknown Artist";

    /*
     * IMPORTANT:
     *
     * This URI is YOUR backend URI.
     *
     * Android should use this URI DIRECTLY.
     */

    const streamUrl =
        `${process.env.BACKEND_PUBLIC_URL || "http://192.168.1.5:6969"}` +
        `/api/media/stream/${encodeURIComponent(track.id)}`;

    const result: AudiusTrack = {
        id: track.id,
        title: track.title,
        artist,
        uri: streamUrl
    };

    if (
        track.duration !== undefined
    ) {
        result.duration =
            track.duration;
    }

    const coverArt =
        track.artwork?.["480x480"] ??
        track.artwork?.["150x150"];

    if (coverArt) {
        result.coverArt =
            coverArt;
    }

    return result;
};


/*
|--------------------------------------------------------------------------
| Search Audius
|--------------------------------------------------------------------------
*/

export const searchAudius = async (
    query: string
): Promise<AudiusTrack[]> => {

    const normalized =
        query
            .trim()
            .toLowerCase();

    if (!normalized) {
        return [];
    }

    let songQuery = normalized;
    let artistQuery = "";

    /*
     * Example:
     *
     * believer by imagine dragons
     *
     * songQuery   = believer
     * artistQuery = imagine dragons
     */

    const byIndex =
        normalized.lastIndexOf(" by ");

    if (byIndex > 0) {

        songQuery =
            normalized
                .substring(0, byIndex)
                .trim();

        artistQuery =
            normalized
                .substring(byIndex + 4)
                .trim();
    }

    /*
     * Search combined query
     */

    const combinedResults =
        await searchAudiusRaw(normalized);

    let songResults: AudiusApiTrack[] = [];
    let artistResults: AudiusApiTrack[] = [];

    if (artistQuery) {

        songResults =
            await searchAudiusRaw(
                songQuery
            );

        artistResults =
            await searchAudiusRaw(
                artistQuery
            );
    }

    /*
     * Merge all results
     */

    const allTracks = [
        ...combinedResults,
        ...songResults,
        ...artistResults
    ];

    /*
     * Remove duplicates
     */

    const unique =
        Array.from(
            new Map(
                allTracks.map(
                    track => [
                        track.id,
                        track
                    ]
                )
            ).values()
        );

    console.log(
        `[AUDIUS] Unique tracks: ${unique.length}`
    );


    /*
     * Alternate-version words
     */

    const alternateWords = [
        "cover",
        "remix",
        "instrumental",
        "acoustic",
        "fingerstyle",
        "bootleg",
        "karaoke",
        "live",
        "sped up",
        "slowed",
        "edit",
        "mix",
        "version",
        "tribute"
    ];


    /*
     * Rank
     */

    const ranked =
        unique
            .map(track => {

                const title =
                    track.title
                        .toLowerCase()
                        .trim();

                const artist =
                    track.user?.name
                        ?.toLowerCase()
                        .trim() || "";

                let score = 0;


                /*
                 * Exact song title
                 */

                if (
                    title === songQuery
                ) {
                    score += 500;
                }

                else if (
                    title.includes(songQuery)
                ) {
                    score += 250;
                }


                /*
                 * Artist matching
                 */

                if (artistQuery) {

                    if (
                        artist === artistQuery
                    ) {
                        score += 1000;
                    }

                    else if (
                        artist.includes(
                            artistQuery
                        )
                    ) {
                        score += 700;
                    }

                    /*
                     * Artist mentioned in title
                     */

                    if (
                        title.includes(
                            artistQuery
                        )
                    ) {
                        score += 100;
                    }
                }


                /*
                 * Penalize alternate versions
                 */

                for (
                    const word
                    of alternateWords
                ) {

                    if (
                        title.includes(word)
                    ) {
                        score -= 300;
                    }
                }


                /*
                 * Combined query exact-ish
                 */

                if (
                    title.includes(
                        songQuery
                    ) &&
                    artistQuery &&
                    title.includes(
                        artistQuery
                    )
                ) {
                    score += 100;
                }


                return {
                    track,
                    score
                };
            })
            .sort(
                (a, b) =>
                    b.score - a.score
            );


    /*
     * Check streamability BEFORE returning.
     *
     * This prevents Android from receiving
     * dead Audius tracks.
     */

    const playable: {
        track: AudiusApiTrack;
        score: number;
    }[] = [];

    for (
        const item of ranked
    ) {

        if (
            playable.length >= 10
        ) {
            break;
        }

        const streamable =
            await isTrackStreamable(
                item.track.id
            );

        if (!streamable) {
            continue;
        }

        console.log(
            `[AUDIUS] PLAYABLE: ` +
            `${item.track.title} | ` +
            `${item.track.user?.name || "Unknown"}`
        );

        playable.push(item);
    }


    console.log(
        `[AUDIUS] Playable tracks: ` +
        `${playable.length}/${ranked.length}`
    );


    /*
     * Final conversion
     */

    const results: AudiusTrack[] = [];

    for (const item of playable) {

        const converted =
            convertTrack(item.track);

        if (converted) {
            results.push(converted);
        }
    }

    console.log(
        "[AUDIUS] TOP RANKED PLAYABLE RESULTS:"
    );

    playable.forEach((item, index) => {

        console.log(
            `[AUDIUS] ${index + 1}. ` +
            `${item.track.title} | ` +
            `${item.track.user?.name || "Unknown"} | ` +
            `score=${item.score}`
        );
    });


    return results;
};