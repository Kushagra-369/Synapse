package com.synapse.mobile.features.skills.youtube.gateway

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidYouTubeGateway(
    private val context: Context
) : YouTubeGateway {

    override fun open(): Boolean {

        return try {

            val intent = context.packageManager
                .getLaunchIntentForPackage("com.google.android.youtube")
                ?: Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com")
                )

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun search(query: String): Boolean {

        return try {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.youtube.com/results?search_query=$query"
                )
            )

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun play(query: String): Boolean {

        return search(query)

    }

}