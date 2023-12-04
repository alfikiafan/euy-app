package com.example.myapplication.DataDummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataFood (
    val nameFood: String,
    val photoFood: Int,
    val photoClock:Int,
    val reqDesc:String,

    ) : Parcelable