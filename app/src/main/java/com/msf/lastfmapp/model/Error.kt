package com.msf.lastfmapp.model

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("error")
    val code: Int,
    val message: String
)