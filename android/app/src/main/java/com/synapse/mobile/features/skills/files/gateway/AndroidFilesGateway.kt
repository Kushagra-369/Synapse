package com.synapse.mobile.features.skills.files.gateway

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment

class AndroidFilesGateway(
    private val context: Context
) : FilesGateway {

    private fun openFolder(path: String): Boolean {

        return try {

            val intent = Intent(Intent.ACTION_VIEW).apply {

                setDataAndType(
                    Uri.parse(path),
                    "*/*"
                )

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun openDownloads(): Boolean {

        return openFolder(
            Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
                .toURI()
                .toString()
        )

    }

    override fun openDocuments(): Boolean {

        return openFolder(
            Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )
                .toURI()
                .toString()
        )

    }

    override fun openImages(): Boolean {

        return openFolder(
            Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )
                .toURI()
                .toString()
        )

    }

    override fun openVideos(): Boolean {

        return openFolder(
            Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES
                )
                .toURI()
                .toString()
        )

    }

}