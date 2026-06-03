package com.karunadakala.source.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.karunadakala.source.model.ArtForm
import com.karunadakala.source.model.Artisan
import com.karunadakala.source.model.CulturalEvent
import com.karunadakala.source.model.MarkerKind

internal fun DocumentSnapshot.toArtFormOrNull(): ArtForm? {
    val resolvedId = getString("id") ?: id
    val name = getString("name") ?: return null
    val history = getString("history").orEmpty()
    val region = getString("region").orEmpty()
    val videoUrl = getString("videoUrl").orEmpty()
    val imageUrl = getString("imageUrl").orEmpty()
    val originDistrict = getString("originDistrict").orEmpty()
    val famousArtisans = (get("famousArtisans") as? List<*>)?.mapNotNull { it as? String }.orEmpty()
    val workshopAvailable = getBoolean("workshopAvailable") ?: false
    return ArtForm(
        id = resolvedId,
        name = name,
        history = history,
        region = region,
        videoUrl = videoUrl,
        imageUrl = imageUrl,
        originDistrict = originDistrict,
        famousArtisans = famousArtisans,
        workshopAvailable = workshopAvailable,
    )
}

internal fun DocumentSnapshot.toArtisanOrNull(): Artisan? {
    val resolvedId = getString("id") ?: id
    val name = getString("name") ?: return null
    val artForm = getString("artForm").orEmpty()
    val district = getString("district").orEmpty()
    val phone = getString("phone").orEmpty()
    val imageUrl = getString("imageUrl").orEmpty()
    val latitude = getDouble("latitude") ?: return null
    val longitude = getDouble("longitude") ?: return null
    val bio = getString("bio").orEmpty()
    val experienceYears = (getLong("experienceYears") ?: 0L).toInt()
    val village = getString("village").orEmpty()
    val markerKind = when (getString("markerKind")?.uppercase()) {
        "PERFORMANCE" -> MarkerKind.PERFORMANCE
        else -> MarkerKind.WORKSHOP
    }
    val workshops = (get("workshops") as? List<*>)?.mapNotNull { it as? String }.orEmpty()
    val products = (get("products") as? List<*>)?.mapNotNull { it as? String }.orEmpty()
    return Artisan(
        id = resolvedId,
        name = name,
        artForm = artForm,
        district = district,
        phone = phone,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude,
        bio = bio,
        experienceYears = experienceYears,
        village = village,
        markerKind = markerKind,
        workshops = workshops,
        products = products,
    )
}

internal fun DocumentSnapshot.toEventOrNull(): CulturalEvent? {
    val resolvedId = getString("id") ?: id
    val title = getString("title") ?: return null
    val artForm = getString("artForm").orEmpty()
    val district = getString("district").orEmpty()
    val date = getString("date").orEmpty()
    val venue = getString("venue").orEmpty()
    val imageUrl = getString("imageUrl").orEmpty()
    return CulturalEvent(
        id = resolvedId,
        title = title,
        artForm = artForm,
        district = district,
        date = date,
        venue = venue,
        imageUrl = imageUrl,
    )
}
