package com.synapse.mobile.features.storage

import android.net.Uri
import java.io.File

interface FileGateway {

    fun readText(file: File): String?

    fun writeText(
        file: File,
        text: String
    ): Boolean

    fun delete(file: File): Boolean

    fun exists(file: File): Boolean

    fun createFile(
        name: String
    ): File?

    fun listFiles(): List<File>

    fun getFileFromUri(
        uri: Uri
    ): File?
}