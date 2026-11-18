package com.coronado.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coronado.campusgps.ui.PantallaMapa
import com.coronado.campusgps.ui.theme.CampusGPSTheme
import com.coronado.campusgps.viewmodel.CampusViewModelFactory
import com.coronado.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                val vm: MapaViewModel = viewModel(
                    factory = CampusViewModelFactory(applicationContext)
                )
                PantallaMapa(vm)
            }
        }
    }
}