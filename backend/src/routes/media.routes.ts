import { Router } from "express";
import {
    searchMedia,
    streamMedia
} from "../controllers/media.controller";

const router = Router();

router.get("/search", searchMedia);

router.get("/stream/:trackId", streamMedia);

export default router;