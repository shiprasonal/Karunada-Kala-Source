package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.model.Artisan
import com.karunadakala.source.repository.ArtisanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ArtisanProfileViewModel(
    artisanId: String,
    artisanRepository: ArtisanRepository,
) : ViewModel() {

    val artisan: StateFlow<Artisan?> =
        flow { emit(artisanRepository.getArtisan(artisanId)) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
