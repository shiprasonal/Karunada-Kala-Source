package com.karunadakala.source.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.karunadakala.source.repository.FirebaseGuestAuth
import com.karunadakala.source.model.ArtForm
import com.karunadakala.source.model.WorkshopRegistration
import com.karunadakala.source.repository.ArtFormRepository
import com.karunadakala.source.repository.WorkshopRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val IsoDateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")

object WorkshopExperienceLevels {
    val options = listOf("Beginner", "Intermediate", "Advanced", "Professional / Guru-led")
}

sealed interface WorkshopRegistrationUiEvent {
    data object Success : WorkshopRegistrationUiEvent
    data class Error(val message: String) : WorkshopRegistrationUiEvent
}

class WorkshopRegistrationViewModel(
    private val artFormRepository: ArtFormRepository,
    private val workshopRepository: WorkshopRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val prefillArtFormId: String =
        savedStateHandle.get<String>("prefillArtFormId")
            ?.takeIf { it.isNotBlank() && it != "_" }
            .orEmpty()

    val artForms: StateFlow<List<ArtForm>> =
        artFormRepository.observeArtForms()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val fullName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val preferredDate = MutableStateFlow("")
    val experienceLevel = MutableStateFlow(WorkshopExperienceLevels.options.first())

    private val _selectedArtFormId = MutableStateFlow<String?>(null)
    val selectedArtFormId: StateFlow<String?> = _selectedArtFormId

    private val _submitting = MutableStateFlow(false)
    val submitting: StateFlow<Boolean> = _submitting

    private val _events = Channel<WorkshopRegistrationUiEvent>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            val list = artFormRepository.observeArtForms().first()
            if (list.isEmpty()) return@launch
            val match = list.find { it.id == prefillArtFormId } ?: list.firstOrNull()
            _selectedArtFormId.value = match?.id
        }
    }

    fun selectArtForm(id: String?) {
        _selectedArtFormId.value = id
    }

    fun submit() {
        val name = fullName.value.trim()
        val mail = email.value.trim()
        val tel = phone.value.trim()
        val date = preferredDate.value.trim()
        val exp = experienceLevel.value.trim()
        val forms = artForms.value
        val selectedId = _selectedArtFormId.value
        val selectedForm = forms.find { it.id == selectedId }

        if (name.length < 2) {
            emitError("Please enter your full name.")
            return
        }
        if (!mail.contains('@') || mail.length < 5) {
            emitError("Please enter a valid email.")
            return
        }
        if (tel.length < 8) {
            emitError("Please enter a valid phone number.")
            return
        }
        if (selectedForm == null) {
            emitError("Please select an art form.")
            return
        }
        if (!IsoDateRegex.matches(date)) {
            emitError("Preferred date must be YYYY-MM-DD.")
            return
        }
        if (exp.isBlank()) {
            emitError("Please choose an experience level.")
            return
        }

        viewModelScope.launch {
            _submitting.value = true
            FirebaseGuestAuth.ensureAnonymousSession()

            val registration = WorkshopRegistration(
                userName = name,
                email = mail,
                phone = tel,
                artForm = selectedForm.name,
                artFormId = selectedForm.id,
                date = date,
                experienceLevel = exp,
            )

            val result = workshopRepository.submit(registration)
            _submitting.value = false

            result.fold(
                onSuccess = { _events.send(WorkshopRegistrationUiEvent.Success) },
                onFailure = { e ->
                    _events.send(
                        WorkshopRegistrationUiEvent.Error(
                            e.message ?: "Could not submit registration. Check Firebase setup and network.",
                        ),
                    )
                },
            )
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _events.send(WorkshopRegistrationUiEvent.Error(message))
        }
    }
}

class WorkshopRegistrationViewModelFactory(
    private val artFormRepository: ArtFormRepository,
    private val workshopRepository: WorkshopRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(WorkshopRegistrationViewModel::class.java))
        return WorkshopRegistrationViewModel(
            artFormRepository,
            workshopRepository,
            savedStateHandle,
        ) as T
    }
}
