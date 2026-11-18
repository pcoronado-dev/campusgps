package com.coronado.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coronado.campusgps.model.Lugar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ultimo_lugar_prefs")

interface UltimoLugarRepository {
    val ultimoLugarId: Flow<Int?>
    suspend fun guardarUltimoLugar(lugar: Lugar)
}

class UltimoLugarRepositoryDataStore(private val context: Context) : UltimoLugarRepository {

    private object PreferencesKeys {
        val LUGAR_ID = intPreferencesKey("lugar_id")
        val LUGAR_NOMBRE = stringPreferencesKey("lugar_nombre")
        val LUGAR_LAT = doublePreferencesKey("lugar_lat")
        val LUGAR_LNG = doublePreferencesKey("lugar_lng")
    }

    override val ultimoLugarId: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LUGAR_ID]
        }

    override suspend fun guardarUltimoLugar(lugar: Lugar) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LUGAR_ID] = lugar.id
            preferences[PreferencesKeys.LUGAR_NOMBRE] = lugar.nombre
            preferences[PreferencesKeys.LUGAR_LAT] = lugar.latitud
            preferences[PreferencesKeys.LUGAR_LNG] = lugar.longitud
        }
    }
}
