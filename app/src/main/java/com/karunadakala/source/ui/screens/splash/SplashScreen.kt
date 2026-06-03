package com.karunadakala.source.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.karunadakala.source.repository.UserPreferencesRepository
import com.karunadakala.source.navigation.Routes
import com.karunadakala.source.ui.theme.KarnatakaRed
import com.karunadakala.source.ui.theme.KarnatakaYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    userPreferencesRepository: UserPreferencesRepository,
    onNavigate: (String) -> Unit,
) {
    var animateIn by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "splashFade",
    )

    LaunchedEffect(Unit) {
        animateIn = true
        delay(900)
        val onboardingDone = userPreferencesRepository.onboardingCompleted.first()
        val destination = if (onboardingDone) Routes.Main else Routes.Onboarding
        delay(450)
        onNavigate(destination)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            KarnatakaYellow.copy(alpha = 0.35f),
                            MaterialTheme.colorScheme.background,
                            KarnatakaRed.copy(alpha = 0.18f),
                        ),
                    ),
                ),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .alpha(alpha),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .size(92.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(KarnatakaYellow, KarnatakaRed.copy(alpha = 0.85f))),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "ಕ",
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "Karunada-Kala Source",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Preserving Karnataka’s Living Heritage",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
