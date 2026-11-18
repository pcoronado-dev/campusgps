package com.coronado.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coronado.campusgps.data.LugaresRepositoryLocal
import com.coronado.campusgps.data.UbicacionNoDisponibleException
import com.coronado.campusgps.data.UbicacionRepository
import com.coronado.campusgps.model.Lugar
import com.coronado.campusgps.model.LugarConDistancia
import com.coronado.campusgps.model.Posicion
import com.coronado.campusgps.model.TipoError
import com.coronado.campusgps.model.UbicacionUi
import com.coronado.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepositoryLocal
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
        Log.d("CampusGPS", _lugares.value.joinToString { "${it.lugar.nombre}=${it.metros}" })
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación",
            TipoError.OTRO
        )
    }
}