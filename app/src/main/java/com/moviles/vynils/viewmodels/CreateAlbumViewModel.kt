package com.moviles.vynils.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.moviles.vynils.models.Album
import com.moviles.vynils.models.NewAlbum
import com.moviles.vynils.repositories.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAlbumViewModel(application: Application) :  AndroidViewModel(application) {
    private val albumsRepository = AlbumRepository(application)

    private val _album = MutableLiveData<Album>()

    val album: LiveData<Album>
        get() = _album

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    //fun sendDataFromNetwork(name:String, cover:String, releaseDate:String, description:String, genre:String, recordLabel:String ) {
    fun sendDataFromNetwork(newAlbum: NewAlbum) {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO){
                    val data = albumsRepository.sendData(newAlbum)
                    _album.postValue(data)
                }
            }
            _eventNetworkError.postValue(false)
            _isNetworkErrorShown.postValue(false)
        }
        catch (e:Exception){
            Log.d("Error", e.toString())
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateAlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                Log.d("Factory-Crear album", "ingreso a CreateAlbumViewModel")
                return CreateAlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}