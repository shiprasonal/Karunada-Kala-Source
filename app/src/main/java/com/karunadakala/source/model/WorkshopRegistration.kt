package com.karunadakala.source.model

data class WorkshopRegistration(
    val id: String = "",
    val userName: String,
    val email: String,
    val phone: String,
    /** Display name of the art form (matches Firestore `artForm` field). */
    val artForm: String,
    /** Optional stable id when selected from catalog (stored as `artFormId`). */
    val artFormId: String = "",
    val date: String,
    val experienceLevel: String,
)
