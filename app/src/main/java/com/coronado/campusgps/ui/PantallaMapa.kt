package com.coronado.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.coronado.campusgps.model.UbicacionUi
import com.coronado.campusgps.util.formatearDistancia
import com.coronado.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()
    val contexto = LocalContext.current

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }

    val lanzadorSeguimiento = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.iniciarSeguimiento()
        } else {
            vm.permisoDenegado()
        }
    }

    fun alPulsar() {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun alPulsarSeguimiento() {
        if (siguiendo) {
            vm.detenerSeguimiento()
        } else {
            val fine = ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val coarse = ContextCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (fine || coarse) {
                vm.iniciarSeguimiento()
            } else {
                lanzadorSeguimiento.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val error = estado as? UbicacionUi.Error

        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (error == null) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.id,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { alPulsar() },
                        enabled = estado !is UbicacionUi.Buscando && !siguiendo
                    ) { Text("Obtener mi ubicación") }

                    Button(
                        onClick = { alPulsarSeguimiento() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (siguiendo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (siguiendo) "Detener seguimiento" else "Iniciar seguimiento")
                    }
                }

                if (error != null) {
                    MensajeError(error = error, onReintentar = { alPulsar() })
                } else {
                    when (estado) {
                        UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                        UbicacionUi.Buscando -> CircularProgressIndicator()
                        else -> Unit
                    }

                    OutlinedButton(
                        onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                        enabled = masCercano != null
                    ) { Text("Ir al más cercano") }

                    masCercano?.let { cercano ->
                        val metros = lugares.firstOrNull { it.lugar.id == cercano.id }?.metros
                        Text(
                            "Más cercano: ${cercano.nombre} (${metros?.let { formatearDistancia(it) } ?: "-"})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            if (error == null) {
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { vm.seleccionar(it) },
                    masCercanoId = masCercano?.id,
                    modifier = Modifier.fillMaxWidth().height(220.dp)
                )
            }
        }
    }
}