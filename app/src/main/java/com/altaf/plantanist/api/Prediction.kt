package com.altaf.plantanist.api

data class Prediction(
    val id: String,
    val plant: String,
    val condition: String,
    val description: String,
    val confidence_score: Double,
    val date: String,
    val timestamp: String,
    val image_url: String
)