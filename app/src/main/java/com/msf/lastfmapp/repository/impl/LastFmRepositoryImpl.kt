package com.msf.lastfmapp.repository.impl

import com.msf.lastfmapp.BuildConfig
import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.network.Service
import com.msf.lastfmapp.repository.LastFmRepository

class LastFmRepositoryImpl(private val service: Service) : LastFmRepository{

    override suspend fun fetchMusics(query: String) : MusicResult = service.fetchTracks(query, BuildConfig.API_KEY)
}