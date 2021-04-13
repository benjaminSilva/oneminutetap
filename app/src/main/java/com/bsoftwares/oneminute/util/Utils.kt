package com.bsoftwares.oneminute.util

import android.app.Activity
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService

private val HALF_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

enum class BuzzType(val pattern: LongArray) {
    HALF_WAY(HALF_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

enum class TimerState {
    STOPPED, PAUSED, RUNNING
}

fun buzz(pattern: LongArray, activity : Activity?) {
    val buzzer = activity?.getSystemService<Vibrator>()

    buzzer?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            //deprecated in API 26
            buzzer.vibrate(pattern, -1)
        }
    }
}
