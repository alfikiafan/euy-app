package com.example.myapplication.DataDummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataRecom (
        val nameRecom: String,
        val photoRecom: Int,
) : Parcelable