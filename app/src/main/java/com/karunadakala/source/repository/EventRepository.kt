package com.karunadakala.source.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.karunadakala.source.repository.SampleData
import com.karunadakala.source.model.CulturalEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface EventRepository {
    fun observeEvents(): Flow<List<CulturalEvent>>
}

class OfflineFirstEventRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : EventRepository {

    override fun observeEvents(): Flow<List<CulturalEvent>> = callbackFlow {
        trySend(SampleData.events)

        lateinit var registration: ListenerRegistration
        registration = firestore.collection("events").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val docs = snapshot?.documents.orEmpty()
            if (docs.isEmpty()) return@addSnapshotListener

            val mapped = docs.mapNotNull { it.toEventOrNull() }
            if (mapped.isNotEmpty()) trySend(mapped)
        }

        awaitClose { registration.remove() }
    }
}
