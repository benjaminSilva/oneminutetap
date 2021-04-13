package com.bsoftwares.oneminute.model

import com.bsoftwares.oneminute.database.SettingsDB

data class TimerSettings(
    val id : Int,
    var timer : Int,
    var vibration : Boolean,
    var sound : Boolean,
    var repeat : Boolean
)

fun TimerSettings.toDatabase() : SettingsDB{
    return SettingsDB(
        id = id,
        timer = timer,
        vibration = if (vibration) 1 else 0,
        sound = if (sound) 1 else 0,
        repeat = if (repeat) 1 else 0
    )
}