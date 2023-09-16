package haveFun.inThis.onlineGame.goFair.parts.iSayDisco.gameLogics

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.extentions.MutableLiveDataWithDefault
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NormalGameLogic(partyNumber: Int): ViewModel(), GameLogic {
    override val canClick = MutableLiveDataWithDefault(value = false, default = false)
    override val picked = MutableLiveDataWithDefault(0, 0)
    override val timer = MutableLiveDataWithDefault(60 - 2 * (partyNumber - 6 - 1), 60 - 2 * (partyNumber - 6 - 1))
    override var stopTimer = false
    override val partyOrder: List<Int> = when(partyNumber) {
        7 -> listOf(0,1,2,3,4,5)
        8 -> listOf(0,2,1,3,4,5)
        9 -> listOf(3,2,1,0,5,4)
        10 -> listOf(3,1,4,2,5,0)
        11 -> listOf(2,4,3,1,0,5)
        12 -> listOf(2,5,1,0,3,4)
        13 -> listOf(4,2,3,1,5,0)
        14 -> listOf(3,0,2,4,5,1)
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
            delay(1000)
        }
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