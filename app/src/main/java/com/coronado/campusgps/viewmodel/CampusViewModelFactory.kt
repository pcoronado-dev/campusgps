package com.coronado.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coronado.campusgps.data.LugaresRepositoryLocal
import com.coronado.campusgps.data.UbicacionRepositoryGoogle
import com.coronado.campusgps.data.UltimoLugarRepositoryDataStore

class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(
            UbicacionRepositoryGoogle(context.applicationContext),
            LugaresRepositoryLocal(),
            UltimoLugarRepositoryDataStore(context.applicationContext)
        ) as T
}