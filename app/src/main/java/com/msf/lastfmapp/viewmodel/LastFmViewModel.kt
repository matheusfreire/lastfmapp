package com.msf.lastfmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msf.lastfmapp.model.Error
import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.model.Results
import com.msf.lastfmapp.network.ResultWrapper.GenericError
import com.msf.lastfmapp.network.ResultWrapper.Success
import com.msf.lastfmapp.repository.LastFmRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LastFmViewModel(private val lastFmRepository: LastFmRepository) : ViewModel() {

    private var searchJob: Job? = null

    private val _mutableLiveDataResults = MutableLiveData<Results>()
    val liveDataResults = _mutableLiveDataResults

    private val _mutableLoading = MutableLiveData<Boolean>()
    val liveDataLoading = _mutableLoading

    private val _mutableError = MutableLiveData<Error>()
    val liveDataError = _mutableError

    fun fetchTracks(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _mutableLoading.postValue(true)
            when(val result = lastFmRepository.fetchMusics(query)) {
                is Success -> handleSuccess(result)
                is GenericError -> _mutableError.postValue(result.error)
                else -> _mutableError.postValue(Error(999, "Something went wrong"))
            }
            _mutableLoading.postValue(false)
        }
    }

    private fun handleSuccess(result: Success<MusicResult>) {
        val totalResults = result.value.results?.totalResults?.toInt() ?: 0
        if (totalResults > 0) {
            _mutableLiveDataResults.postValue(result.value.results)
        } else {
            _mutableError.postValue(Error(1, "No musics founded with your criteria"))
        }
    }
}