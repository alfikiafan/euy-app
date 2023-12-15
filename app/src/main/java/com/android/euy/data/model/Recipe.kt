package com.android.euy.data.model

import java.io.Serializable

data class Recipe(
    val id : String,
    val name : String,
    val ingredients : List<String>,
    val steps : List<String>,
    val description : String,
    val image : String
) : Serializable
