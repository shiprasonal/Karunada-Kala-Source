package com.karunadakala.source

import android.content.Context
import com.karunadakala.source.repository.ArtisanRepository
import com.karunadakala.source.repository.ArtFormRepository
import com.karunadakala.source.repository.EventRepository
import com.karunadakala.source.repository.FirestoreWorkshopRepository
import com.karunadakala.source.repository.OfflineFirstArtFormRepository
import com.karunadakala.source.repository.OfflineFirstArtisanRepository
import com.karunadakala.source.repository.OfflineFirstEventRepository
import com.karunadakala.source.repository.UserPreferencesRepository
import com.karunadakala.source.repository.WorkshopRepository

/** Dependency holder wiring repositories for ViewModels and navigation. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val userPreferencesRepository: UserPreferencesRepository = UserPreferencesRepository(appContext)
    val artFormRepository: ArtFormRepository = OfflineFirstArtFormRepository()
    val artisanRepository: ArtisanRepository = OfflineFirstArtisanRepository()
    val eventRepository: EventRepository = OfflineFirstEventRepository()
    val workshopRepository: WorkshopRepository = FirestoreWorkshopRepository()
}
