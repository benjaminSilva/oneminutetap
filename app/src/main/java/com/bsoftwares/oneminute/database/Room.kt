package com.bsoftwares.oneminute.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimerDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSettings(vararg settings : SettingsDB)

    @Query("select * from settingsdb")
    fun getSettings() : LiveData<SettingsDB>

    @Update
    fun updateSettings(vararg settings : SettingsDB)
}

/*@Database(entities = [SettingsDB::class], version = 1)
abstract class TimerDataBase : RoomDatabase() {
    abstract val timerDAO : TimerDao
}*/

@Database(entities = [SettingsDB::class], version = 2,autoMigrations = [AutoMigration(from = 1,to = 2)])
abstract class TimerDataBase : RoomDatabase() {
    abstract val timerDAO : TimerDao
}


private lateinit var INSTANCE : TimerDataBase

fun getDataBase(context: Context) : TimerDataBase{
    if(!::INSTANCE.isInitialized)
        INSTANCE = Room.databaseBuilder(context.applicationContext,
            TimerDataBase::class.java,
            "Timer.db").createFromAsset("defaultDatabase/Timerdb.db").build()
    return INSTANCE
}

//