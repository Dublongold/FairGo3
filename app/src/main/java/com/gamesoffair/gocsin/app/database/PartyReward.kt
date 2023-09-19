package com.gamesoffair.gocsin.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partyReward")
data class PartyReward (
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo val openAt: Long
)