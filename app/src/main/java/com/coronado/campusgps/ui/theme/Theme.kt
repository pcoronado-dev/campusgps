package com.coronado.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulSobreContenedor,
    secondary = AzulPrimarioOscuro,
    onSecondary = Color.White,
    secondaryContainer = AzulContenedor,
    onSecondaryContainer = AzulSobreContenedor,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = FondoClaro,
    onSurface = TextoPrincipal,
    surfaceVariant = SuperficieTarjeta,
    onSurfaceVariant = TextoSecundario,
    outline = AzulPrimario
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioDark,
    onPrimary = Color(0xFF003062),
    primaryContainer = AzulContenedorDark,
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = AzulPrimarioDark,
    onSecondary = Color(0xFF003062),
    background = FondoOscuro,
    onBackground = Color(0xFFE2E2E6),
    surface = FondoOscuro,
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = SuperficieOscura,
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = AzulPrimarioDark
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}