package com.moviles.vynils.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.moviles.vynils.models.Artist
import com.moviles.vynils.repositories.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private val artistRepository = ArtistRepository(application)

    private val _artists = MutableLiveData<List<Artist>>()

    val artists: LiveData<List<Artist>>
       get() = _artists

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    init {
        refreshDataFromNetwork()
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true

    }


    private fun refreshDataFromNetwork()  {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    val listMusicians = artistRepository.refreshData()
                    _artists.postValue(listMusicians)
                }
                _isNetworkErrorShown.postValue(false)
                _eventNetworkError.postValue(false)             }

        } catch(e: Exception) {
            _eventNetworkError.value = true
        }


    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(application) as T

            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }






}