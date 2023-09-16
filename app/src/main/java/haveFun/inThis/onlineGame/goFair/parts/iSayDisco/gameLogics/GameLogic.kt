package haveFun.inThis.onlineGame.goFair.parts.iSayDisco.gameLogics

import android.view.View
import haveFun.inThis.onlineGame.goFair.extentions.MutableLiveDataWithDefault

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