package com.moviles.vynils.repositories

import android.app.Application
import com.moviles.vynils.models.Artist
import com.moviles.vynils.network.NetworkServiceAdapter

class ArtistRepository(val app: Application) {

    suspend fun refreshData(): List<Artist> {
        /*
        val bandas = NetworkServiceAdapter.getInstance(app).getBands()
        val musicos = NetworkServiceAdapter.getInstance(app).getMusicians()
        val artistas : List<Artist> = merge(bandas, musicos)

        return artistas
*/
        return merge(NetworkServiceAdapter.getInstance(app).getBands(), NetworkServiceAdapter.getInstance(app).getMusicians())
    }

    fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first + second
    }

}