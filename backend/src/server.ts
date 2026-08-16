import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import mediaRoutes from "./routes/media.routes";
import { connectDatabase } from "./config/database";
dotenv.config({ quiet: true });

const app = express();

app.use(cors());
app.use(express.json());

app.get("/health", (_req, res) => {
    res.json({
        success: true,
        message: "Synapse Media Backend is running"
    });
});

app.use("/api/media", mediaRoutes);

const PORT = Number(process.env.PORT) || 6969;

connectDatabase();

app.listen(PORT, "0.0.0.0", () => {
    console.log(
        `Synapse Media Backend running on port ${PORT}`
    );
});