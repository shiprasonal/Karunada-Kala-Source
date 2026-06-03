package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.model.Artisan
import com.karunadakala.source.repository.ArtisanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ArtisanMapViewModel(
    artisanRepository: ArtisanRepository,
) : ViewModel() {

    val artisans: StateFlow<List<Artisan>> =
        artisanRepository.observeArtisans()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
