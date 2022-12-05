package com.moviles.vynils.repositories

import android.app.Application
import com.moviles.vynils.models.Artist
import com.moviles.vynils.network.NetworkServiceAdapter

class ArtistRepository(val app: Application) {

    suspend fun refreshData(): List<Artist> {
        return merge(NetworkServiceAdapter.getInstance(app).getBands(), NetworkServiceAdapter.getInstance(app).getMusicians())
    }

    fun <T> merge(first: List<T>, second: List<T>): List<T> {
        return first + second
}

    suspend fun postBand(name: String, image: String, description: String, date: String): String {
        // Log.d("postBand called", name + description + date + image )
        return NetworkServiceAdapter.getInstance(app).postBand(name,image, description,date)
    }

    suspend fun postMusician(name: String, image: String, description: String, date: String): String {
        return NetworkServiceAdapter.getInstance(app).postMusician(name, image, description, date)
    }
}