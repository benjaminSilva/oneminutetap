package com.bsoftwares.oneminute.viewModel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.bsoftwares.oneminute.database.getDataBase
import com.bsoftwares.oneminute.model.TimerSettings
import com.bsoftwares.oneminute.repository.TimerRepository
import com.bsoftwares.oneminute.util.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val dataBase = getDataBase(application)
    private val repository = TimerRepository(dataBase)

    val settings = repository.setting
    val START = "Start"
    val PAUSE = "Paused"

    val remainingTime: LiveData<Int>
        get() = _remainingTime
    var _remainingTime = MutableLiveData<Int>()

    val btn_text : LiveData<String>
        get() = _btn_text
    var _btn_text = MutableLiveData<String>()

    val isPlaying : LiveData<Boolean>
        get() = _isPlaying
    val _isPlaying = MutableLiveData<Boolean>()

    var status = TimerState.STOPPED

    var timer : CountDownTimer? = null

    init {
        _btn_text.value = START
        _isPlaying.value = false
        //_remainingTime.value = settings.value!!.timer
    }

    fun timerController() {
        when (status) {
            TimerState.STOPPED -> startTimer(settings.value!!.timer)
            TimerState.RUNNING -> pauseTimer()
            TimerState.PAUSED -> startTimer(remainingTime.value!!)
        }
    }


    private fun pauseTimer() {
        timer!!.cancel()
        status = TimerState.PAUSED
        _isPlaying.value = false
        _btn_text.value = PAUSE
    }

    private fun startTimer(time : Int){
        status = TimerState.RUNNING
        TimerState.STOPPED
        _isPlaying.value = true
        timer = object : CountDownTimer(time.toLong()*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = (millisUntilFinished / 1000).toInt()
                val tempo = ((millisUntilFinished / 1000).toInt()).toString()
                _btn_text.value = tempo
            }

            override fun onFinish() {
                _btn_text.value = START
                status = TimerState.STOPPED
                _isPlaying.value = false
                if(settings.value!!.repeat)
                    startTimer(settings.value!!.timer)
            }
        }
        timer!!.start()
    }

    fun restartTimer(){
        if(timer!=null)
            timer!!.cancel()
        startTimer(settings.value!!.timer)
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TimerViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}