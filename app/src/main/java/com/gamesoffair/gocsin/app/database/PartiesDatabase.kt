package com.gamesoffair.gocsin.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Party::class, PartyReward::class], version = 1)
abstract class PartiesDatabase: RoomDatabase() {
    abstract fun getDao(): PartiesDao
}