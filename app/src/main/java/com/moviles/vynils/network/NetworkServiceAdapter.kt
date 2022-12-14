package com.moviles.vynils.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.moviles.vynils.models.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://vinylsmovil.uc.r.appspot.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    suspend fun getAlbums() = suspendCoroutine<List<Album>>{ cont->
        requestQueue.add(getRequest("albums/",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),name = item.getString("name"), cover = item.getString("cover"), recordLabel = item.getString("recordLabel"), releaseDate = item.getString("releaseDate"), genre = item.getString("genre"), description = item.getString("description")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getAlbum(albumId:Int) = suspendCoroutine<Album>{ cont->
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                val resp = JSONObject(response)
                val item = Album(albumId = resp.getInt("id"),name = resp.getString("name"), cover = resp.getString("cover"), recordLabel = resp.getString("recordLabel"), releaseDate = resp.getString("releaseDate"), genre = resp.getString("genre"), description = resp.getString("description"))
                cont.resume(item)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun createAlbum(newAlbum: NewAlbum) = suspendCoroutine<Album>{ cont->

        var gson = Gson()
        var jsonString = gson.toJson(newAlbum)
        Log.d("****createAlbum", jsonString)
        requestQueue.add(postRequest("albums/", JSONObject(jsonString),
            { resp ->
                Log.d("****Response", resp.toString())
                val item = Album(albumId = resp.getInt("id"),name = resp.getString("name"), cover = resp.getString("cover"), recordLabel = resp.getString("recordLabel"), releaseDate = resp.getString("releaseDate"), genre = resp.getString("genre"), description = resp.getString("description"))
                cont.resume(item)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollectors() = suspendCoroutine<List<Collector>>{ cont->
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) { //inicializado como variable de retorno
                    item = resp.getJSONObject(i)
                    list.add(i, Collector(collectorId = item.getInt("id"),name = item.getString("name"), telephone = item.getString("telephone"), email = item.getString("email")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getBands() = suspendCoroutine<List<Artist>> { cont ->
        requestQueue.add(getRequest("bands/",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Band>()
                var item: JSONObject? = null
                var artist: Band? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    artist = Band(id = item.getInt("id"), name=item.getString("name"),
                        image = item.getString("image"), description = item.getString("description"),
                        creationdate = item.getString("creationDate"))
                    list.add(artist)
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getMusicians() = suspendCoroutine<List<Artist>> { cont ->
        requestQueue.add(getRequest("musicians/",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Artist>()
                var item: JSONObject? = null
                var artist: Musician? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    artist = Musician(id = item.getInt("id"), name=item.getString("name"),
                        image = item.getString("image"), description = item.getString("description"),
                        birthdate = item.getString("birthDate"))
                    list.add(artist)
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollector(collectorId:Int) = suspendCoroutine<Collector>{ cont->
        requestQueue.add(getRequest("collectors/$collectorId",
            { response ->
                val resp = JSONObject(response)
                val item = Collector(collectorId = resp.getInt("id"), name = resp.getString("name"), telephone = resp.getString("telephone"), email = resp.getString("email"))
                cont.resume(item)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun postBand(name: String, image: String, description: String, date: String) = suspendCoroutine<String> { cont ->

        val postParams = mapOf<String, Any>(
            "name" to name,
            "description" to description,
            "creationDate" to date,
            "image" to image
        )
        requestQueue.add(postRequest("bands/", JSONObject(postParams),
            {response ->
                cont.resume(response.toString())

            },
            {
                cont.resumeWithException(it)
            }
        ))

    }

    suspend fun postMusician(name: String, image: String, description: String, date: String) = suspendCoroutine<String> { cont ->

        val postParams = mapOf<String, Any>(
            "name" to name,
            "description" to description,
            "birthDate" to date,
            "image" to image
        )
        requestQueue.add(postRequest("musicians/", JSONObject(postParams),
            {response ->
                cont.resume(response.toString())

            },
            {
                cont.resumeWithException(it)
            }
        ))

    }


    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }

    private fun postRequest(path: String, body: JSONObject, responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ): JsonObjectRequest {
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }

    private fun putRequest(path: String, body: JSONObject, responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ): JsonObjectRequest {
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}