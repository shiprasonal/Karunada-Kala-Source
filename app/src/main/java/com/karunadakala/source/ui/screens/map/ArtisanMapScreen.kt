package com.karunadakala.source.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.karunadakala.source.model.Artisan
import com.karunadakala.source.model.MarkerKind
import com.karunadakala.source.ui.theme.KarnatakaRed
import com.karunadakala.source.ui.theme.KarnatakaYellow
import com.karunadakala.source.utils.openDialer
import com.karunadakala.source.utils.rememberMarkerDescriptor
import com.karunadakala.source.viewmodel.ArtisanMapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtisanMapScreen(
    viewModel: ArtisanMapViewModel,
    onViewProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val artisans by viewModel.artisans.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var locationGranted by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { grants ->
        locationGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        locationGranted = fine || coarse
        if (!locationGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(14.85, 76.25), 6.9f)
    }

    LaunchedEffect(artisans) {
        val list = artisans
        if (list.isEmpty()) return@LaunchedEffect
        runCatching {
            when (list.size) {
                1 -> {
                    val a = list.first()
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(LatLng(a.latitude, a.longitude), 10f),
                        450,
                    )
                }
                else -> {
                    val builder = LatLngBounds.builder()
                    list.forEach { builder.include(LatLng(it.latitude, it.longitude)) }
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngBounds(builder.build(), 148),
                        600,
                    )
                }
            }
        }
    }

    var selectedArtisan by remember { mutableStateOf<Artisan?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val workshopMarkerIcon = rememberMarkerDescriptor(MarkerKind.WORKSHOP)
    val performanceMarkerIcon = rememberMarkerDescriptor(MarkerKind.PERFORMANCE)

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = locationGranted),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = locationGranted,
                compassEnabled = true,
                mapToolbarEnabled = true,
            ),
        ) {
            artisans.forEach { artisan ->
                key(artisan.id) {
                    val markerState = remember(artisan.id) {
                        MarkerState(position = LatLng(artisan.latitude, artisan.longitude))
                    }
                    val icon = when (artisan.markerKind) {
                        MarkerKind.WORKSHOP -> workshopMarkerIcon
                        MarkerKind.PERFORMANCE -> performanceMarkerIcon
                    }
                    Marker(
                        state = markerState,
                        title = artisan.name,
                        snippet = artisan.artForm,
                        icon = icon,
                        onClick = {
                            selectedArtisan = artisan
                            true
                        },
                    )
                }
            }
        }

        MapLegend(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
        )
    }

    selectedArtisan?.let { artisan ->
        ModalBottomSheet(
            onDismissRequest = { selectedArtisan = null },
            sheetState = sheetState,
        ) {
            ArtisanMarkerBottomSheet(
                artisan = artisan,
                onDismiss = { selectedArtisan = null },
                onDial = { context.openDialer(artisan.phone) },
                onViewProfile = {
                    val id = artisan.id
                    selectedArtisan = null
                    onViewProfile(id)
                },
            )
        }
    }
}

@Composable
private fun MapLegend(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 3.dp,
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LegendSwatch(color = KarnatakaYellow, label = "Workshop")
            LegendSwatch(color = KarnatakaRed, label = "Performance")
        }
    }
}

@Composable
private fun LegendSwatch(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            Modifier
                .size(12.dp)
                .background(color, CircleShape),
        )
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun ArtisanMarkerBottomSheet(
    artisan: Artisan,
    onDismiss: () -> Unit,
    onDial: () -> Unit,
    onViewProfile: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = artisan.name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = artisan.artForm,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Column {
                Text("${artisan.village}, ${artisan.district}", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Lat ${"%.4f".format(artisan.latitude)}, Lng ${"%.4f".format(artisan.longitude)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
            shape = RoundedCornerShape(14.dp),
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Contact", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(artisan.phone, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium))
            }
        }

        HorizontalDivider()

        OutlinedButton(
            onClick = onDial,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
        ) {
            Icon(Icons.Outlined.Call, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tap to call")
        }

        Button(
            onClick = onViewProfile,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
        ) {
            Icon(Icons.Outlined.Person, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("View profile")
        }

        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
        ) {
            Text("Close")
        }

        Spacer(Modifier.height(8.dp))
    }
}
