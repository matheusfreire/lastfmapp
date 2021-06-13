package com.msf.lastfmapp.network

import com.msf.lastfmapp.model.MusicResult
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("?method=track.search&format=json&limit=10")
    suspend fun fetchTracks(
        @Query("track") track: String,
        @Query("api_key") apiKey: String
    ): MusicResult
}