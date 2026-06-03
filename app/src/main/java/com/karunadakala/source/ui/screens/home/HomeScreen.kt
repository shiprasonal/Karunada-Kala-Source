package com.karunadakala.source.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.karunadakala.source.ui.components.FeaturedBannerCarousel
import com.karunadakala.source.ui.components.HomeCategoriesSection
import com.karunadakala.source.ui.components.HomeCategory
import com.karunadakala.source.ui.components.KarunadaHomeTopBar
import com.karunadakala.source.ui.components.SectionHeader
import com.karunadakala.source.ui.components.UpcomingEventsRow
import com.karunadakala.source.ui.components.WorkshopHostsRow
import com.karunadakala.source.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onOpenExploreTab: () -> Unit,
    onArtFormClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val featured by viewModel.featuredArtForms.collectAsStateWithLifecycle()
    val upcoming by viewModel.upcomingEvents.collectAsStateWithLifecycle()
    val hosts by viewModel.workshopHosts.collectAsStateWithLifecycle()

    val categories = listOf(
        HomeCategory("Performance Arts", "Theatre, dance-drama, percussion spectacles"),
        HomeCategory("Handicrafts", "Metal, lacquer, weaving & miniature arts"),
        HomeCategory("Workshops", "Hands-on intensives with verified mentors"),
        HomeCategory("Events", "Festivals, nights & heritage showcases"),
        HomeCategory("Artisan Market", "Ethical crafts sourced from clusters"),
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            KarunadaHomeTopBar(
                onSearchClick = onSearchClick,
                onProfileClick = onProfileClick,
            )
        }

        item {
            FeaturedBannerCarousel(
                items = featured,
                onArtClick = onArtFormClick,
            )
        }

        item {
            SectionHeader(title = "Categories")
        }

        item {
            HomeCategoriesSection(
                categories = categories,
                onCategoryClick = { onOpenExploreTab() },
            )
        }

        item {
            SectionHeader(title = "Upcoming events preview")
        }

        item {
            UpcomingEventsRow(events = upcoming)
        }

        item {
            SectionHeader(title = "Nearby workshops preview")
        }

        item {
            WorkshopHostsRow(artisans = hosts)
        }
    }
}
