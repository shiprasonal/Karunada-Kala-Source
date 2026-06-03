package com.karunadakala.source.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object FirebaseGuestAuth {

    suspend fun ensureAnonymousSession(): Result<Unit> = runCatching {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) return@runCatching
        auth.signInAnonymously().await()
        Unit
    }
}
