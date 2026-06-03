package com.karunadakala.source.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.karunadakala.source.model.WorkshopRegistration
import kotlinx.coroutines.tasks.await

interface WorkshopRepository {
    suspend fun submit(registration: WorkshopRegistration): Result<Unit>
}

class FirestoreWorkshopRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : WorkshopRepository {

    override suspend fun submit(registration: WorkshopRegistration): Result<Unit> = runCatching {
        val payload = buildMap<String, Any> {
            put("userName", registration.userName)
            put("email", registration.email)
            put("phone", registration.phone)
            put("artForm", registration.artForm)
            put("date", registration.date)
            put("experienceLevel", registration.experienceLevel)
            put("createdAt", FieldValue.serverTimestamp())
            if (registration.artFormId.isNotBlank()) {
                put("artFormId", registration.artFormId)
            }
        }
        firestore.collection("workshops").add(payload).await()
        Unit
    }
}
