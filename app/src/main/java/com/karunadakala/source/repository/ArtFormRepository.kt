package com.karunadakala.source.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.karunadakala.source.repository.SampleData
import com.karunadakala.source.model.ArtForm
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/** Art catalog: offline [SampleData] first, optional Firestore merge. */
interface ArtFormRepository {
    fun observeArtForms(): Flow<List<ArtForm>>
    suspend fun getArtForm(id: String): ArtForm?
}

class OfflineFirstArtFormRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ArtFormRepository {

    override fun observeArtForms(): Flow<List<ArtForm>> = callbackFlow {
        trySend(SampleData.artForms)

        lateinit var registration: ListenerRegistration
        registration = firestore.collection("artForms").addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val docs = snapshot?.documents.orEmpty()
            if (docs.isEmpty()) return@addSnapshotListener

            val mapped = docs.mapNotNull { it.toArtFormOrNull() }
            if (mapped.isNotEmpty()) trySend(mapped)
        }

        awaitClose { registration.remove() }
    }

    override suspend fun getArtForm(id: String): ArtForm? {
        SampleData.artForms.find { it.id == id }?.let { return it }

        val snap = runCatching { firestore.collection("artForms").document(id).get().await() }.getOrNull()
            ?: return null
        return snap.toArtFormOrNull()
    }
}
