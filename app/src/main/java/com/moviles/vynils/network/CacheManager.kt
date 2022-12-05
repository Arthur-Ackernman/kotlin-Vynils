package com.moviles.vynils.network

import android.content.Context
import com.moviles.vynils.models.Album
import com.moviles.vynils.models.Artist
import com.moviles.vynils.models.Collector

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getIntance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    private var albums: HashMap<Int, Album> = hashMapOf()
    private var artists: HashMap<Int, Artist> = hashMapOf()
    private var collectors: HashMap<Int, Collector> = hashMapOf()

    fun addAlbum(albumId: Int, album: Album){
        if (!albums.containsKey(albumId)){
            albums[albumId] = album
        }
    }

    fun getAlbum(albumId: Int) : Album {
        return if (albums.containsKey(albumId)) albums[albumId]!! else Album(0, "", "", "", "", "", "")
    }

    fun addArtist(artistId: Int, artist: Artist) {
        if (!artists.containsKey(artistId)) {
            artists[artistId] = artist
        }
    }

    fun getArtist(artistId: Int): Artist? {
        return if (artists.containsKey(artistId)) artists[artistId]!! else null
    }

    fun getCollector(collectorId: Int) : Collector? {
        return if (collectors.containsKey(collectorId)) collectors[collectorId]!! else null
    }
}