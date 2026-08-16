import { Router } from "express";
import { searchMedia } from "../controllers/media.controller";

const router = Router();

router.get("/search", searchMedia);

export default router;