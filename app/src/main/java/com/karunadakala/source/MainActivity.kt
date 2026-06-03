package com.karunadakala.source

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.karunadakala.source.ui.theme.KarunadaTheme

/** Single-activity entry point hosting the Compose navigation graph. */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = AppContainer(this)

        setContent {
            KarunadaTheme {
                CompositionLocalProvider(LocalAppContainer provides appContainer) {
                    KarunadaApp()
                }
            }
        }
    }
}
