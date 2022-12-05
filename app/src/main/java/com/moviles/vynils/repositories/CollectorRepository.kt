package com.moviles.vynils.repositories

import android.app.Application
import android.util.Log
import com.moviles.vynils.models.Collector
import com.moviles.vynils.network.CacheManager
import com.moviles.vynils.network.NetworkServiceAdapter

class CollectorRepository (val application: Application){
    suspend fun refreshData(): List<Collector> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getCollectors()
    }

    suspend fun refreshData(collectorId: Int): Collector {
        var collectorRespCache = CacheManager.getIntance(application.applicationContext).getCollector(collectorId)
        return if(collectorRespCache == null) {
            Log.d("Cache decision", "get from network")
            var collector = NetworkServiceAdapter.getInstance(application).getCollector(collectorId)
            CacheManager.getIntance(application.applicationContext).addCollector(collectorId, collector)
            collector
        }else{
            Log.d("Cache decision", "return $collectorRespCache elements from cache")
            collectorRespCache!!
        }
    }
}

