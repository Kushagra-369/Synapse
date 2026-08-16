import dotenv from "dotenv";
import { connectDatabase } from "../config/database";
import { Media } from "../models/media.model";

dotenv.config();

const seedMedia = async () => {
    try {
        await connectDatabase();

        await Media.deleteMany({});

        await Media.insertMany([
            {
                title: "test",
                artist: "Synapse",
                album: "Test Album",
                duration: 180,
                uri: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
            },

            {
                title: "test 2",
                artist: "Synapse",
                album: "Test Album",
                duration: 200,
                uri: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
            },

            {
                title: "test 3",
                artist: "Synapse",
                album: "Test Album",
                duration: 220,
                uri: "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
            }
        ]);

        console.log("Media seeded successfully");

    } catch (error) {

        console.error(
            "Media seed failed:",
            error
        );

    } finally {

        process.exit(0);
    }
};

seedMedia();