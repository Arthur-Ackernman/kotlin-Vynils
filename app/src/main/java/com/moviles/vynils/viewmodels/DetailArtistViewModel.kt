package com.moviles.vynils.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

import com.moviles.vynils.models.Artist

import com.moviles.vynils.repositories.ArtistRepository

class DetailArtistViewModel(app: Application, artist: Artist) : AndroidViewModel(app){

    private val _artist = MutableLiveData<Artist>()

    val artist: LiveData<Artist>
        get() = _artist

    init {
        _artist.value = artist
    }

    class Factory(val app: Application, val artist: Artist) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                Log.d("FacDetailAlbumViewModel", "ingreso a DetailAlbumViewModel")
                return DetailArtistViewModel(app, artist) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
