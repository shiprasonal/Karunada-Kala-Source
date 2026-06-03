package com.karunadakala.source.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.karunadakala.source.repository.SampleData
import com.karunadakala.source.model.Artisan
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface ArtisanRepository {
    fun observeArtisans(): Flow<List<Artisan>>
    suspend fun getArtisan(id: String): Artisan?
}

class OfflineFirstArtisanRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ArtisanRepository {

    override fun observeArtisans(): Flow<List<Artisan>> = callbackFlow {
        trySend(SampleData.artisans)

        lateinit var registration: ListenerRegistration
        registration = firestore.collection("artisans").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val docs = snapshot?.documents.orEmpty()
            if (docs.isEmpty()) return@addSnapshotListener

            val mapped = docs.mapNotNull { it.toArtisanOrNull() }
            if (mapped.isNotEmpty()) trySend(mapped)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun getArtisan(id: String): Artisan? {
        SampleData.artisans.find { it.id == id }?.let { return it }

        val snap = runCatching { firestore.collection("artisans").document(id).get().await() }.getOrNull()
            ?: return null
        return snap.toArtisanOrNull()
    }
}
