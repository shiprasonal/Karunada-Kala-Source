package com.karunadakala.source.ui.screens.workshop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karunadakala.source.viewmodel.WorkshopExperienceLevels
import com.karunadakala.source.viewmodel.WorkshopRegistrationUiEvent
import com.karunadakala.source.viewmodel.WorkshopRegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopRegistrationScreen(
    viewModel: WorkshopRegistrationViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val artForms by viewModel.artForms.collectAsStateWithLifecycle()
    val selectedArtFormId by viewModel.selectedArtFormId.collectAsStateWithLifecycle()
    val submitting by viewModel.submitting.collectAsStateWithLifecycle()

    val fullName by viewModel.fullName.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val preferredDate by viewModel.preferredDate.collectAsStateWithLifecycle()
    val experienceLevel by viewModel.experienceLevel.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                WorkshopRegistrationUiEvent.Success ->
                    snackbarHostState.showSnackbar("Registration submitted successfully!")
                is WorkshopRegistrationUiEvent.Error ->
                    snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    var artMenuExpanded by remember { mutableStateOf(false) }
    var expMenuExpanded by remember { mutableStateOf(false) }

    val selectedArtForm = artForms.find { it.id == selectedArtFormId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Workshop registration",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Your details are stored in Cloud Firestore under the `workshops` collection (anonymous sign-in is used when needed).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { viewModel.fullName.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full name") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
            )

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { viewModel.phone.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
            )

            ExposedDropdownMenuBox(
                expanded = artMenuExpanded,
                onExpandedChange = { artMenuExpanded = !artMenuExpanded },
            ) {
                OutlinedTextField(
                    value = selectedArtForm?.name.orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Selected art form") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = artMenuExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    shape = RoundedCornerShape(14.dp),
                )
                ExposedDropdownMenu(
                    expanded = artMenuExpanded,
                    onDismissRequest = { artMenuExpanded = false },
                ) {
                    artForms.forEach { art ->
                        DropdownMenuItem(
                            text = { Text(art.name) },
                            onClick = {
                                viewModel.selectArtForm(art.id)
                                artMenuExpanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = preferredDate,
                onValueChange = { viewModel.preferredDate.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Preferred date (YYYY-MM-DD)") },
                placeholder = { Text("2026-06-15") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                supportingText = { Text("Use ISO format so coordinators can sort registrations.") },
            )

            ExposedDropdownMenuBox(
                expanded = expMenuExpanded,
                onExpandedChange = { expMenuExpanded = !expMenuExpanded },
            ) {
                OutlinedTextField(
                    value = experienceLevel,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Experience level") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expMenuExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    shape = RoundedCornerShape(14.dp),
                )
                ExposedDropdownMenu(
                    expanded = expMenuExpanded,
                    onDismissRequest = { expMenuExpanded = false },
                ) {
                    WorkshopExperienceLevels.options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.experienceLevel.value = option
                                expMenuExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.submit() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !submitting && artForms.isNotEmpty(),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(if (submitting) "Submitting…" else "Submit registration")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
