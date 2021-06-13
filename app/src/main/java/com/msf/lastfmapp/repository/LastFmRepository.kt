package com.msf.lastfmapp.repository

import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.network.Service

interface LastFmRepository {
    suspend fun fetchMusics(query: String) : MusicResult
}