package com.karunadakala.source.model

data class ArtForm(
    val id: String,
    val name: String,
    val history: String,
    val region: String,
    val videoUrl: String,
    val imageUrl: String,
    val originDistrict: String,
    val famousArtisans: List<String>,
    val workshopAvailable: Boolean,
)
