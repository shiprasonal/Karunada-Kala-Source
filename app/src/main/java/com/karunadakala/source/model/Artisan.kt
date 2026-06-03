package com.karunadakala.source.model

data class Artisan(
    val id: String,
    val name: String,
    val artForm: String,
    val district: String,
    val phone: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val bio: String,
    val experienceYears: Int,
    val village: String,
    val markerKind: MarkerKind,
    val workshops: List<String>,
    val products: List<String>,
)
