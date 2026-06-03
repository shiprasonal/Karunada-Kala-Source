package com.karunadakala.source.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.model.ArtForm
import com.karunadakala.source.repository.ArtFormRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ArtDetailViewModel(
    artFormId: String,
    artFormRepository: ArtFormRepository,
) : ViewModel() {

    val artForm: StateFlow<ArtForm?> =
        flow { emit(artFormRepository.getArtForm(artFormId)) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
