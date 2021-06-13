package com.msf.lastfmapp.model

import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("opensearch:Query")
    val openSearch: OpenSearchQuery,

    @SerializedName("opensearch:itemsPerPage")
    val itemsPerPage: String,

    @SerializedName("opensearch:startIndex")
    val startIndex: String,

    @SerializedName("opensearch:totalResults")
    val totalResults: String,

    val trackmatches: TrackMatches
)