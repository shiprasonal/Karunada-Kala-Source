package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.repository.SampleData
import com.karunadakala.source.model.ArtForm
import com.karunadakala.source.model.Artisan
import com.karunadakala.source.model.CulturalEvent
import com.karunadakala.source.model.MarkerKind
import com.karunadakala.source.repository.ArtFormRepository
import com.karunadakala.source.repository.ArtisanRepository
import com.karunadakala.source.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    artFormRepository: ArtFormRepository,
    eventRepository: EventRepository,
    artisanRepository: ArtisanRepository,
) : ViewModel() {

    val featuredArtForms: StateFlow<List<ArtForm>> =
        artFormRepository.observeArtForms()
            .map { list ->
                val map = list.associateBy { it.id }
                SampleData.featuredBannerIds.mapNotNull { map[it] }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val upcomingEvents: StateFlow<List<CulturalEvent>> =
        eventRepository.observeEvents()
            .map { events -> events.sortedBy { it.date }.take(5) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val workshopHosts: StateFlow<List<Artisan>> =
        artisanRepository.observeArtisans()
            .map { artisans ->
                artisans.filter { it.markerKind == MarkerKind.WORKSHOP }.take(5)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
