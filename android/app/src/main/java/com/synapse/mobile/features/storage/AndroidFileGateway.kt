package com.synapse.mobile.features.storage

import android.content.Context
import android.net.Uri
import java.io.File

class AndroidFileGateway(
    private val context: Context
) : FileGateway {

    override fun readText(
        file: File
    ): String? {

        return try {
            file.readText()
        } catch (e: Exception) {
            null
        }

    }

    override fun writeText(
        file: File,
        text: String
    ): Boolean {

        return try {
            file.writeText(text)
            true
        } catch (e: Exception) {
            false
        }

    }

    override fun delete(
        file: File
    ): Boolean {

        return file.delete()

    }

    override fun exists(
        file: File
    ): Boolean {

        return file.exists()

    }

    override fun createFile(
        name: String
    ): File? {

        return try {

            val file = File(
                context.filesDir,
                name
            )

            if (!file.exists()) {
                file.createNewFile()
            }

            file

        } catch (e: Exception) {
            null
        }

    }

    override fun listFiles(): List<File> {

        return context.filesDir
            .listFiles()
            ?.toList()
            ?: emptyList()

    }

    override fun getFileFromUri(
        uri: Uri
    ): File? {

        return try {
            File(uri.path ?: return null)
        } catch (e: Exception) {
            null
        }

    }

}