package com.msf.lastfmapp.repository

import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.network.ResultWrapper

interface LastFmRepository {
    suspend fun fetchMusics(query: String) : ResultWrapper<MusicResult>
}