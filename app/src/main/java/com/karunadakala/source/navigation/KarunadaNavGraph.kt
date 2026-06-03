package com.karunadakala.source.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karunadakala.source.LocalAppContainer
import com.karunadakala.source.ui.screens.artisan.ArtisanProfileScreen
import com.karunadakala.source.ui.screens.events.EventsScreen
import com.karunadakala.source.ui.screens.profile.ProfileScreen
import com.karunadakala.source.ui.screens.detail.ArtDetailScreen
import com.karunadakala.source.ui.screens.explore.ArtFormExplorerScreen
import com.karunadakala.source.ui.screens.home.HomeScreen
import com.karunadakala.source.ui.screens.map.ArtisanMapScreen
import com.karunadakala.source.ui.screens.onboarding.OnboardingScreen
import com.karunadakala.source.ui.screens.splash.SplashScreen
import com.karunadakala.source.ui.screens.workshop.WorkshopRegistrationScreen
import com.karunadakala.source.utils.viewModelFactory
import com.karunadakala.source.viewmodel.ArtDetailViewModel
import com.karunadakala.source.viewmodel.ArtisanMapViewModel
import com.karunadakala.source.viewmodel.ArtisanProfileViewModel
import com.karunadakala.source.viewmodel.EventsViewModel
import com.karunadakala.source.viewmodel.ExploreViewModel
import com.karunadakala.source.viewmodel.HomeViewModel
import com.karunadakala.source.viewmodel.WorkshopRegistrationViewModel
import com.karunadakala.source.viewmodel.WorkshopRegistrationViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val bottomDestinations = listOf(
    BottomDestination(Routes.Home, "Home", Icons.Outlined.Home),
    BottomDestination(Routes.Explore, "Explore", Icons.Outlined.Explore),
    BottomDestination(Routes.Map, "Map", Icons.Outlined.Map),
    BottomDestination(Routes.Events, "Events", Icons.Outlined.Event),
    BottomDestination(Routes.Profile, "Profile", Icons.Outlined.Person),
)

/** Root navigation: splash → onboarding → main shell with nested bottom-nav destinations. */
@Composable
fun KarunadaNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val container = LocalAppContainer.current
            SplashScreen(
                userPreferencesRepository = container.userPreferencesRepository,
                onNavigate = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.Onboarding) {
            val container = LocalAppContainer.current
            val scope = rememberCoroutineScope()
            OnboardingScreen(
                onSkip = {
                    scope.launch {
                        container.userPreferencesRepository.setOnboardingCompleted(true)
                        navController.navigate(Routes.Main) {
                            popUpTo(Routes.Onboarding) { inclusive = true }
                        }
                    }
                },
                onFinished = {
                    scope.launch {
                        container.userPreferencesRepository.setOnboardingCompleted(true)
                        navController.navigate(Routes.Main) {
                            popUpTo(Routes.Onboarding) { inclusive = true }
                        }
                    }
                },
            )
        }

        composable(Routes.Main) {
            MainShell(
                rootNavController = navController,
            )
        }

        composable(
            route = Routes.ArtDetailPattern,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) {
            val container = LocalAppContainer.current
            val artFormId = it.arguments?.getString("id").orEmpty()
            val vm: ArtDetailViewModel = viewModel(
                factory = viewModelFactory {
                    ArtDetailViewModel(artFormId, container.artFormRepository)
                },
            )
            val artForm by vm.artForm.collectAsStateWithLifecycle()
            ArtDetailScreen(
                artForm = artForm,
                onBack = { navController.popBackStack() },
                onRegisterForWorkshop = {
                    artForm?.let { navController.navigate(Routes.workshopRegistration(it.id)) }
                },
            )
        }

        composable(
            route = Routes.ArtisanDetailPattern,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { entry ->
            val container = LocalAppContainer.current
            val artisanId = entry.arguments?.getString("id").orEmpty()
            val scope = rememberCoroutineScope()
            val vm: ArtisanProfileViewModel = viewModel(
                factory = viewModelFactory {
                    ArtisanProfileViewModel(artisanId, container.artisanRepository)
                },
            )
            val artisan by vm.artisan.collectAsStateWithLifecycle()
            ArtisanProfileScreen(
                artisan = artisan,
                onBack = { navController.popBackStack() },
                onRegisterWorkshop = { artisanItem ->
                    scope.launch {
                        val forms = container.artFormRepository.observeArtForms().first()
                        val match = forms.find { it.name.equals(artisanItem.artForm, ignoreCase = true) }
                            ?: forms.firstOrNull { artisanItem.artForm.contains(it.name, ignoreCase = true) }
                        val id = match?.id ?: "_"
                        navController.navigate(Routes.workshopRegistration(id))
                    }
                },
            )
        }

        composable(
            route = Routes.WorkshopRegistrationPattern,
            arguments = listOf(
                navArgument("prefillArtFormId") {
                    type = NavType.StringType
                    defaultValue = "_"
                },
            ),
        ) { entry ->
            val container = LocalAppContainer.current
            val vm: WorkshopRegistrationViewModel = viewModel(
                viewModelStoreOwner = entry,
                factory = WorkshopRegistrationViewModelFactory(
                    container.artFormRepository,
                    container.workshopRepository,
                    entry.savedStateHandle,
                ),
            )
            WorkshopRegistrationScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun MainShell(rootNavController: NavHostController) {
    val innerNav = rememberNavController()
    val navBackStackEntry by innerNav.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomDestinations.forEach { destination ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == destination.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            innerNav.navigate(destination.route) {
                                popUpTo(innerNav.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = innerNav,
            startDestination = Routes.Home,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.Home) {
                val container = LocalAppContainer.current
                val vm: HomeViewModel = viewModel(
                    factory = viewModelFactory {
                        HomeViewModel(
                            container.artFormRepository,
                            container.eventRepository,
                            container.artisanRepository,
                        )
                    },
                )

                HomeScreen(
                    viewModel = vm,
                    onSearchClick = {
                        innerNav.navigate(Routes.Explore) {
                            popUpTo(innerNav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onProfileClick = {
                        innerNav.navigate(Routes.Profile) {
                            popUpTo(innerNav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenExploreTab = {
                        innerNav.navigate(Routes.Explore) {
                            popUpTo(innerNav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onArtFormClick = { id ->
                        rootNavController.navigate(Routes.artDetail(id))
                    },
                )
            }

            composable(Routes.Explore) {
                val container = LocalAppContainer.current
                val vm: ExploreViewModel = viewModel(
                    factory = viewModelFactory {
                        ExploreViewModel(container.artFormRepository)
                    },
                )
                ArtFormExplorerScreen(
                    viewModel = vm,
                    onLearnMore = { id -> rootNavController.navigate(Routes.artDetail(id)) },
                )
            }

            composable(Routes.Map) {
                val container = LocalAppContainer.current
                val vm: ArtisanMapViewModel = viewModel(
                    factory = viewModelFactory {
                        ArtisanMapViewModel(container.artisanRepository)
                    },
                )
                ArtisanMapScreen(
                    viewModel = vm,
                    onViewProfile = { id -> rootNavController.navigate(Routes.artisanDetail(id)) },
                )
            }

            composable(Routes.Events) {
                val container = LocalAppContainer.current
                val vm: EventsViewModel = viewModel(
                    factory = viewModelFactory {
                        EventsViewModel(container.eventRepository)
                    },
                )
                EventsScreen(viewModel = vm)
            }

            composable(Routes.Profile) {
                ProfileScreen(
                    onRegisterWorkshop = {
                        rootNavController.navigate(Routes.workshopRegistration("_"))
                    },
                    onOpenMap = {
                        innerNav.navigate(Routes.Map) {
                            popUpTo(innerNav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}
