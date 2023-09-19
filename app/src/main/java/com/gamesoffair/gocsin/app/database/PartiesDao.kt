package com.gamesoffair.gocsin.app.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PartiesDao {
    @Query("select * from parties")
    fun getParties(): List<Party>

    @Query("update parties set unlocked = 1 where partyNumber = :partyNumber")
    fun partyUnlocked(partyNumber: Int)

    @Query("delete from parties")
    fun clearParties()

    @Query("insert into parties(partyNumber, unlocked) values(:partyNumber, :unlocked)")
    fun addParty(partyNumber: Int, unlocked: Int)

    @Query("update partyReward set openAt = :openAt")
    fun updateOpenAt(openAt: Long)

    @Query("select * from partyReward")
    fun getOpenAt(): List<PartyReward>

    @Query("insert into partyReward values(1, 0)")
    fun setOpenAtDefault()

    @Query("delete from partyReward")
    fun deleteOpenAt()

}