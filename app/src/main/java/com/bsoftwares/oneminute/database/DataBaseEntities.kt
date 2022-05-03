package com.bsoftwares.oneminute.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bsoftwares.oneminute.model.TimerSettings
import org.jetbrains.annotations.NotNull

@Entity
data class SettingsDB constructor(
    @PrimaryKey
    val id : Int?,
    val timer : Int?,
    val vibration : Int?,
    val sound : Int?,
    val repeat : Int?,
    val random : Int?,
    val min : Int?,
    val max : Int?
)
fun SettingsDB.toDomain() : TimerSettings{
    return TimerSettings(
        id = id!!,
        timer = timer!!,
        vibration = vibration == 1,
        sound = sound == 1,
        repeat = repeat == 1,
        random = random == 1,
        max = max ?: 0,
        min = min ?: 0
    )
}