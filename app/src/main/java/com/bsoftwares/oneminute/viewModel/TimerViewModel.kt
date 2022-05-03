package com.bsoftwares.oneminute.viewModel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.database.getDataBase
import com.bsoftwares.oneminute.repository.TimerRepository
import com.bsoftwares.oneminute.util.TimerState
import kotlinx.coroutines.*
import kotlin.random.Random

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val CANCEL_MESSAGE = "PAUSE"
    }

    private val dataBase = getDataBase(application)
    private val repository = TimerRepository(dataBase)
    private var coroutineJob : Job? = null

    val settings = repository.setting

    val remainingTime: LiveData<Int>
        get() = _remainingTime
    var _remainingTime = MutableLiveData<Int>()

    val btn_text : LiveData<Any>
        get() = _btn_text
    var _btn_text = MutableLiveData<Any>()

    val isPlaying : LiveData<Boolean>
        get() = _isPlaying
    val _isPlaying = MutableLiveData<Boolean>()

    val max : LiveData<Int>
        get() = _max
    val _max = MutableLiveData<Int>()

    var status = TimerState.STOPPED

    var timer : CountDownTimer? = null

    init {
        _btn_text.value = R.string.start
        _isPlaying.value = false
    }

    fun timerController() {
        when (status) {
            TimerState.STOPPED -> restartTimer()
            TimerState.RUNNING -> pauseTimer()
            TimerState.PAUSED -> startTimer(remainingTime.value!!)
        }
    }


    private fun pauseTimer() {
        coroutineJob?.cancel(CancellationException(CANCEL_MESSAGE))
    }

    private fun startTimer(time : Int){
        status = TimerState.RUNNING
        _isPlaying.value = true
        var remainingTime = time
        coroutineJob = viewModelScope.launch {
            try {
                while (remainingTime>= 0){
                    _remainingTime.postValue(remainingTime)
                    _btn_text.postValue(remainingTime.toString())
                    delay(1000)
                    remainingTime -= 1
                }
                _btn_text.value = R.string.start
                status = TimerState.STOPPED
                _isPlaying.value = false
                if(settings.value!!.repeat)
                    restartTimer()
                else if (settings.value!!.random){
                    _max.postValue(null)
                }
            } catch (e : CancellationException){
                if (e.message == CANCEL_MESSAGE){
                    status = TimerState.PAUSED
                    _isPlaying.value = false
                    _btn_text.value = R.string.paused
                }
            }
        }
    }

    fun restartTimer(){
        coroutineJob?.cancel()
        _max.value = if (settings.value!!.random) Random.nextInt(settings.value!!.min,settings.value!!.max) else settings.value!!.timer
        startTimer(_max.value!!)
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