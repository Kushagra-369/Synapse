package com.synapse.mobile.features.skills.gallery.gateway

interface GalleryGateway {

    fun openGallery(): Boolean

    fun openPhotos(): Boolean

    fun openVideos(): Boolean

}