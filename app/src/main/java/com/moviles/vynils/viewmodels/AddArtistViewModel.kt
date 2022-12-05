package com.moviles.vynils.viewmodels

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.moviles.vynils.repositories.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddArtistViewModel(app: Application): AndroidViewModel(app) {

    private val artistRepository = ArtistRepository(app)

    val listTypes = listOf("Band", "Musician")



    private var _artistType = MutableStateFlow("")
    val artistype = _artistType.asStateFlow()
    private var _artistName = MutableStateFlow("")
    private var _artistDescription = MutableStateFlow("")
    private var _artistImage = MutableStateFlow("")
    private var _artistCreationDate = MutableStateFlow("")


    val isAddEnabled: Flow<Boolean> =
        combine(_artistName, _artistDescription,
            _artistImage, _artistCreationDate, _artistType){ name, description, image, creationDate, type ->
            val isName = !name.isNullOrBlank()
            val isDescription = !description.isNullOrBlank()
            val isImage = image.isValidUrl()
            val isCreationDate = !creationDate.isNullOrBlank()
            val isType = !type.isNullOrBlank()
            return@combine isName and isDescription and isImage and isCreationDate and isType
        }

    fun setType(type: String) {
        _artistType.value = type
    }


    fun setArtistName(name: String) {
        _artistName.value = name
        //  validateForm()
    }

    fun setArtistDescription(description: String) {
        _artistDescription.value = description
    }

    fun setArtistImage(image: String) {
        _artistImage.value = image
        //  validateForm()
    }

    fun setArtistCreationDate(creationDate: String) {
        _artistCreationDate.value = creationDate
        //validateForm()
    }



    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    fun onMessageShown() {
        _isNetworkErrorShown.value = true
    }

    fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

//    private fun validateForm() {
//        if (!_artistName.value.isNullOrBlank()
//            && !_artistDescription.value.isNullOrBlank()
//            && !_artistImage.value.isNullOrBlank() && !_artistCreationDate.value.isNullOrBlank()) {
//            _isSubmitEnabled.value = true
//
//        }
//
//    }


    fun saveArtist() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    if(_artistType.value == listTypes[0]) {
                        val response = artistRepository.postBand(
                            _artistName.value, _artistImage.value, _artistDescription.value,
                            _artistCreationDate.value
                        )
                        Log.d("saveBand", response)
                    } else {
                        val newDate = parseDate(_artistCreationDate.value)
                        val response = artistRepository.postMusician( _artistName.value, _artistImage.value,
                            _artistDescription.value,
                            newDate)
                        Log.d("saveMusician", newDate)
                    }
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                }
            }

        } catch (e: Exception) {
            _eventNetworkError.value = true

        }

    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddArtistViewModel(application) as T

            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }

    }

    fun parseDate(date: String): String {
        val arrayS = date.split("/")
        var newDate = ""
        for(i in arrayS.size -1 downTo 0) {
            if (i != 0) {
                newDate = newDate + arrayS[i] + "/"
            } else {
                newDate = newDate + arrayS[i]
            }
        }

        return newDate

    }

}