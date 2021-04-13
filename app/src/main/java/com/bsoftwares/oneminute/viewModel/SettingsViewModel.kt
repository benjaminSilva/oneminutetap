package com.bsoftwares.oneminute.viewModel

import android.app.Application
import android.media.MediaPlayer
import android.provider.Settings
import androidx.lifecycle.*
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.database.getDataBase
import com.bsoftwares.oneminute.model.TimerSettings
import com.bsoftwares.oneminute.repository.TimerRepository
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val dataBase = getDataBase(application)
    private val repository = TimerRepository(dataBase)

    val settings = repository.setting

    val sound : LiveData<Boolean>
        get() = _sound
    private val _sound = MutableLiveData<Boolean>()

    val vibration : LiveData<Boolean>
        get() = _vibration
    private val _vibration = MutableLiveData<Boolean>()

    val replay : LiveData<String>
        get() = _replay
    private val _replay = MutableLiveData<String>()

    fun timerController(mais : Boolean) {
        val newSettings = settings.value
        if (mais)
            newSettings!!.timer += 5
        else{
            if (newSettings!!.timer > 10){
                newSettings.timer -= 5
            }
        }
        updateSettings(newSettings!!)
    }

    private fun updateSettings(newSettings: TimerSettings) {
        viewModelScope.launch {
            repository.updateSettings(newSettings)
        }
    }

    fun vibrationController() {
        val newSettings = settings.value
        if (!newSettings!!.vibration)
            _vibration.value = true
        newSettings.vibration = !newSettings.vibration
        updateSettings(newSettings)
    }

    fun soundController(){
        val newSettings = settings.value
        if (!newSettings!!.sound)
            _sound.value = true
        newSettings.sound = !newSettings.sound
        updateSettings(newSettings)
    }

    fun replayController(){
        val newSettings = settings.value
        if (!newSettings!!.repeat)
            _replay.value = "Enabled"
        else
            _replay.value = "Disabled"
        newSettings.repeat = !newSettings.repeat
        updateSettings(newSettings)
    }

    lateinit var schedule: TimerTask
    var repeated = false

    fun repeatTimerEquation(mais: Boolean) {
        schedule = Timer().schedule(200) {
            timerController(mais)
            repeatTimerEquation(mais)
            repeated = true
        }
    }

    fun resetSettings(){
        val newSettings = settings.value!!
        newSettings.repeat = false
        newSettings.sound = true
        newSettings.vibration = true
        newSettings.timer = 60
        updateSettings(newSettings)
    }


    fun stopRepeat(mais : Boolean) {
        if (!repeated)
            timerController(mais)
        repeated = false
        schedule.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}