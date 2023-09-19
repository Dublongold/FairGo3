package com.gamesoffair.gocsin.app.parts.iSayDisco.gameLogics

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.extentions.MutableLiveDataWithDefault
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HardGameLogic(partyNumber: Int): ViewModel(), GameLogic {
    override val canClick = MutableLiveDataWithDefault(value = false, default = false)
    override val picked = MutableLiveDataWithDefault(0, 0)
    override val timer = MutableLiveDataWithDefault(60 - 2 * (partyNumber - 14 - 1), 60 - 2 * (partyNumber - 14 - 1))
    override var stopTimer = false
    override val partyOrder: List<Int> = when(partyNumber) {
        15 -> listOf(0,2,7,4,6,1,8,5,3)
        16 -> listOf(3,0,5,7,6,8,2,4,1)
        17 -> listOf(0,1,8,4,2,7,5,3,6)
        18 -> listOf(6,5,8,0,3,2,4,7,1)
        19 -> listOf(8,3,4,1,6,0,2,5,7)
        20 -> listOf(4,1,7,8,6,0,5,3,2)
        else -> throw IllegalArgumentException()
    }
    override val pickedPartiesStack = mutableListOf<Int>()

    override suspend fun startTimer() {
        while(timer.valueNotNull != 0 && !stopTimer) {
            delay(1000)
            if(!stopTimer) {
                timer.valueNotNull -= 1
            }
        }
    }

    override suspend fun startGame(partyElements: List<View>) = coroutineScope {
        launch {
            startTimer()
        }
        Log.i("Start game", "Do something next")
        for(elementId in partyOrder) {
            partyElements[elementId].setBackgroundResource(R.drawable.element_01 + elementId)
            delay(750)
        }
        delay(250)
        for(partyElement in partyElements) {
            partyElement.setBackgroundResource(R.drawable.element_hidden)
        }
        canClick.valueNotNull = true
    }

    override fun pickParty(partyItem: Int): Int {
        var result = if(!pickedPartiesStack.contains(partyItem)) {
            pickedPartiesStack.add(partyItem)
            if(partyOrder[pickedPartiesStack.size - 1] == partyItem) 1 else 0
        }
        else {
            2
        }
        if(pickedPartiesStack.size == partyOrder.size && result == 1) {
            result = 3
            stopTimer = true
        }
        return result
    }

}