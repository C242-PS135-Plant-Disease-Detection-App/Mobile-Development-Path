package com.altaf.plantanist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantResponse(
    val plant: String,
    val disease: String,
    val details: String,
    val confidence: Float
) : Parcelable
