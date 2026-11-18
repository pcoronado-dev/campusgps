package com.coronado.campusgps.data

import com.coronado.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}