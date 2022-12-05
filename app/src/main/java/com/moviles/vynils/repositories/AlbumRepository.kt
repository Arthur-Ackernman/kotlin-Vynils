package com.moviles.vynils.repositories

import android.app.Application
import android.util.Log
import com.moviles.vynils.models.Album
import com.moviles.vynils.models.NewAlbum
import com.moviles.vynils.network.CacheManager
import com.moviles.vynils.network.NetworkServiceAdapter

class AlbumRepository (val application: Application) {
    suspend fun refreshData(): List<Album> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }

    suspend fun refreshData(albumId: Int): Album {
        var albumRespCache = CacheManager.getIntance(application.applicationContext).getAlbum(albumId)
        if(albumRespCache.albumId == 0) {
            Log.d("Cache decision", "get from network")
            var album = NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
            CacheManager.getIntance(application.applicationContext).addAlbum(albumId, album)
            return album
        }else{
            Log.d("Cache decision", "return ${albumRespCache} elements from cache")
            return albumRespCache
        }
    }

    //suspend fun sendData(name:String, cover:String, releaseDate:String, description:String, genre:String, recordLabel:String): Album {
    suspend fun sendData(newAlbum: NewAlbum): Album {
        //return NetworkServiceAdapter.getInstance(application).createAlbum(name, cover, releaseDate, description, genre, recordLabel)
        return NetworkServiceAdapter.getInstance(application).createAlbum(newAlbum)
    }
}