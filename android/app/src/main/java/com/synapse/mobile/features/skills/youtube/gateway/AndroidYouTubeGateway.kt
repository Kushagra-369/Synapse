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

            val encodedQuery =
                Uri.encode(query)

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.youtube.com/results?search_query=$encodedQuery"
                )
            ).apply {

                setPackage("com.google.android.youtube")

                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            try {

                val fallbackIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://www.youtube.com/results?search_query=${Uri.encode(query)}"
                    )
                ).apply {
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                }

                context.startActivity(fallbackIntent)

                true

            } catch (e2: Exception) {

                e2.printStackTrace()

                false
            }
        }
    }

    override fun play(query: String): Boolean {

        return search(query)

    }

}