package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.model.ArtForm
import com.karunadakala.source.repository.ArtFormRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ExploreViewModel(
    private val artFormRepository: ArtFormRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    val searchQuery: StateFlow<String> = query.asStateFlow()

    val artForms: StateFlow<List<ArtForm>> =
        combine(artFormRepository.observeArtForms(), query) { list, raw ->
            val q = raw.trim()
            if (q.isEmpty()) {
                list
            } else {
                list.filter {
                    it.name.contains(q, ignoreCase = true) ||
                        it.region.contains(q, ignoreCase = true) ||
                        it.originDistrict.contains(q, ignoreCase = true) ||
                        it.history.contains(q, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(value: String) {
        query.value = value
    }
}
