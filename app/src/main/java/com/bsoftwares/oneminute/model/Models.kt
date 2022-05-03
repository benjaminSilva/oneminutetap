package com.bsoftwares.oneminute.model

import com.bsoftwares.oneminute.database.SettingsDB

data class TimerSettings(
    val id : Int,
    var timer : Int,
    var vibration : Boolean,
    var sound : Boolean,
    var repeat : Boolean,
    var random : Boolean,
    var min : Int,
    var max : Int
)

fun TimerSettings.toDatabase() : SettingsDB{
    return SettingsDB(
        id = id,
        timer = timer,
        vibration = if (vibration) 1 else 0,
        sound = if (sound) 1 else 0,
        repeat = if (repeat) 1 else 0,
        random = if (random) 1 else 0,
        min = min,
        max = max
    )
}