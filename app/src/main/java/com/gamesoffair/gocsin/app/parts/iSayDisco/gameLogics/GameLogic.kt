package com.gamesoffair.gocsin.app.parts.iSayDisco.gameLogics

import android.view.View
import com.gamesoffair.gocsin.app.extentions.MutableLiveDataWithDefault

interface GameLogic {
    val timer: MutableLiveDataWithDefault<Int>
    val partyOrder: List<Int>
    val pickedPartiesStack: MutableList<Int>
    val canClick: MutableLiveDataWithDefault<Boolean>
    val stopTimer: Boolean
    val picked: MutableLiveDataWithDefault<Int>
    suspend fun startTimer()
    suspend fun startGame(partyElements: List<View>)
    fun pickParty(partyItem: Int): Int
}