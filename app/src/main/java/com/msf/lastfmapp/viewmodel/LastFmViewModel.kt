package com.msf.lastfmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.repository.LastFmRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LastFmViewModel(private val lastFmRepository: LastFmRepository) : ViewModel() {

    private var searchJob: Job? = null

    private val _mutableLiveDataResult = MutableLiveData<MusicResult>()
    val liveDataResult = _mutableLiveDataResult

    private val _mutableLoading = MutableLiveData<Boolean>()
    val liveDataLoading = _mutableLoading

    private val _mutableError = MutableLiveData<Boolean>()
    val liveDataError = _mutableError

    fun fetchTracks(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            debounceJob()
            val result = lastFmRepository.fetchMusics(query)
            _mutableLiveDataResult.postValue(result)
        }
    }

    private suspend fun debounceJob() = delay(300)

}