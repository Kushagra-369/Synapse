package com.synapse.mobile.features.skills.gallery.gateway

import android.content.Context
import android.content.Intent

class AndroidGalleryGateway(
    private val context: Context
) : GalleryGateway {

    override fun openGallery(): Boolean {

        return try {

            val intent = Intent(Intent.ACTION_VIEW).apply {

                type = "image/*"

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun openPhotos(): Boolean {

        return openGallery()

    }

    override fun openVideos(): Boolean {

        return try {

            val intent = Intent(Intent.ACTION_VIEW).apply {

                type = "video/*"

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

}