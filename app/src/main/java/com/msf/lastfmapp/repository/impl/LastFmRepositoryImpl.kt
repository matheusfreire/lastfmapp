package com.msf.lastfmapp.repository.impl

import com.google.gson.Gson
import com.msf.lastfmapp.BuildConfig
import com.msf.lastfmapp.model.Error
import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.network.ResultWrapper
import com.msf.lastfmapp.network.ResultWrapper.Success
import com.msf.lastfmapp.network.ResultWrapper.GenericError
import com.msf.lastfmapp.network.Service
import com.msf.lastfmapp.repository.LastFmRepository
import retrofit2.HttpException

class LastFmRepositoryImpl(private val service: Service) : LastFmRepository {
    override suspend fun fetchMusics(query: String): ResultWrapper<MusicResult> {
        return try {
            val fetchTracks = service.fetchTracks(query, BuildConfig.API_KEY)
            Success(fetchTracks)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson<Error>(errorBody, Error::class.java)
            GenericError(error.code, error)
        }
    }
}