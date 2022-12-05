package com.moviles.vynils.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.moviles.vynils.models.Collector
import com.moviles.vynils.repositories.CollectorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailCollectorViewModel(application: Application, collectorId: Int) : AndroidViewModel(application){

    private val collectorsRepository = CollectorRepository(application)

    private val _collector = MutableLiveData<Collector>()

    val collector: LiveData<Collector>
        get() = _collector

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = collectorId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO){
                    val data = collectorsRepository.refreshData(id)
                    _collector.postValue(data)
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

    class Factory(val app: Application, val collectorId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailCollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                Log.d("FacDetaiCollectorViewModel", "ingreso a DetailCollecorViewModel")
                return DetailCollectorViewModel(app, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

