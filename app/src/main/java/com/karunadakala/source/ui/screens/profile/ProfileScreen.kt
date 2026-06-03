package com.karunadakala.source.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.karunadakala.source.repository.SampleData
import com.karunadakala.source.ui.components.SectionHeader
import com.karunadakala.source.ui.components.WorkshopProgramCard

@Composable
fun ProfileScreen(
    onRegisterWorkshop: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = "Karunada-Kala",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
                Text(
                    text = "Student cultural heritage explorer for Karnataka traditional arts, artisans, events, and workshops.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item {
                Button(
                    onClick = onRegisterWorkshop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text("Register for a workshop")
                }
                OutlinedButton(
                    onClick = onOpenMap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text("Find artisans on map")
                }
            }

            item {
                SectionHeader(title = "Featured workshop programs")
            }

            items(items = SampleData.workshopPrograms, key = { it.id }) { program ->
                WorkshopProgramCard(
                    program = program,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}
