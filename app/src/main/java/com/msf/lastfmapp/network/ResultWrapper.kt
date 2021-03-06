package com.msf.lastfmapp.network

import com.msf.lastfmapp.model.Error

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: Error? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}