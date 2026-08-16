import mongoose, {
    Schema,
    Document
} from "mongoose";

export interface IMedia extends Document {

    title: string;

    artist: string;

    album?: string;

    duration?: number;

    uri: string;

    coverArt?: string;
}

const mediaSchema =
    new Schema<IMedia>(
        {
            title: {
                type: String,
                required: true,
                trim: true,
                index: true
            },

            artist: {
                type: String,
                required: true,
                trim: true,
                index: true
            },

            album: {
                type: String,
                trim: true
            },

            duration: {
                type: Number,
                min: 0
            },

            uri: {
                type: String,
                required: true,
                trim: true
            },

            coverArt: {
                type: String,
                trim: true
            }
        },
        {
            timestamps: true
        }
    );

export const Media =
    mongoose.model<IMedia>(
        "Media",
        mediaSchema
    );