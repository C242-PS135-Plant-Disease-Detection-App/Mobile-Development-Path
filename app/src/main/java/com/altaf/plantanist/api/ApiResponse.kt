package com.altaf.plantanist.api

data class ApiResponse(
    val plant: String,
    val condition: String,
    val description: String,
    val confidence_score: Double,
    val date: String,
    val image_url: String
)