package com.moviles.vynils.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
open class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,

    ) : Parcelable {
}

class Musician(id: Int,name: String, image: String, description: String, val birthdate: String)
    : Artist(id,name,image, description) {

}

class Band(id: Int, name: String, image: String, description: String,  val creationdate: String)
    : Artist(id, name, image, description){

}
