package com.coronado.campusgps.data

import com.coronado.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

class UbicacionNoDisponibleException :
    Exception("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun obtenerPosicionesEnVivo(): Flow<Result<Posicion>>
    fun playServicesDisponible(): Boolean
}
