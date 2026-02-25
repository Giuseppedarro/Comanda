package dev.giuseppedarro.comanda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.use_case.GetThemePreferencesUseCase
import dev.giuseppedarro.comanda.navigation.NavGraph
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val getThemePreferencesUseCase: GetThemePreferencesUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themePreferences by getThemePreferencesUseCase().collectAsState(initial = ThemePreferences(false, false))
            val useDarkTheme = when {
                themePreferences.useSystemTheme -> isSystemInDarkTheme()
                else -> themePreferences.isDarkMode
            }

            ComandaTheme(darkTheme = useDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavGraph()
                }
            }
        }
    }
}
