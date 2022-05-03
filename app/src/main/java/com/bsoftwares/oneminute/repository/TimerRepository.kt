package com.bsoftwares.oneminute.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bsoftwares.oneminute.database.TimerDataBase
import com.bsoftwares.oneminute.database.toDomain
import com.bsoftwares.oneminute.model.TimerSettings
import com.bsoftwares.oneminute.model.toDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimerRepository(private val dataBase: TimerDataBase) {

    val setting : LiveData<TimerSettings> = Transformations.map(dataBase.timerDAO.getSettings()) {
        it.toDomain()
    }

    suspend fun updateSettings(settings: TimerSettings){
        withContext(Dispatchers.IO){
            try {
                dataBase.timerDAO.updateSettings(settings.toDatabase())
            }catch (t:Exception){
                Log.d("Database Error",t.message!!)
            }
        }
    }
}