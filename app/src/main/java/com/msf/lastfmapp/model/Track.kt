package com.msf.lastfmapp.model

import com.google.gson.annotations.SerializedName

data class Track(
    val artist: String,
    @SerializedName("image")
    val images: List<Image>,
    val listeners: String,
    val mbid: String,
    val name: String,
    val streamable: String,
    val url: String
) {
    fun albumPhoto(): String = images.first { it.size == "extralarge" }.text
}