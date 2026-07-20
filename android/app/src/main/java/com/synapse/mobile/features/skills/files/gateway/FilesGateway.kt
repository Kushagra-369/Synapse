package com.synapse.mobile.features.skills.files.gateway

interface FilesGateway {

    fun openDownloads(): Boolean

    fun openDocuments(): Boolean

    fun openImages(): Boolean

    fun openVideos(): Boolean

}