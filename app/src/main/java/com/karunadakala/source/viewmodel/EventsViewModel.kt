package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.model.CulturalEvent
import com.karunadakala.source.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Supplies filtered cultural events for the Events tab (offline-first via [EventRepository]).
 */
class EventsViewModel(
    eventRepository: EventRepository,
) : ViewModel() {

    private val allEvents = eventRepository.observeEvents()
    private val districtFilter = MutableStateFlow<String?>(null)

    val districts: StateFlow<List<String>> =
        allEvents
            .map { events -> events.map { it.district }.distinct().sorted() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val events: StateFlow<List<CulturalEvent>> =
        combine(allEvents, districtFilter) { list, district ->
            val sorted = list.sortedBy { it.date }
            if (district.isNullOrBlank()) sorted else sorted.filter { it.district == district }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val selectedDistrict: StateFlow<String?> = districtFilter

    fun setDistrictFilter(district: String?) {
        districtFilter.value = district
    }
}
