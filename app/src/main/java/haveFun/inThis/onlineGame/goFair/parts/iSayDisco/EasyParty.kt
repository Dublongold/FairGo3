package haveFun.inThis.onlineGame.goFair.parts.iSayDisco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.database.PartiesDatabase
import haveFun.inThis.onlineGame.goFair.extentions.doInflate
import haveFun.inThis.onlineGame.goFair.extentions.doInside
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.gameLogics.EasyGameLogic
import haveFun.inThis.onlineGame.goFair.tools.PartyNumberContainer
import haveFun.inThis.onlineGame.goFair.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class EasyParty: Fragment() {

    private val partyNumberContainer: PartyNumberContainer by inject()
    private val partiesDatabase: PartiesDatabase by inject()

    private lateinit var partyElement1: View
    private lateinit var partyElement2: View
    private lateinit var partyElement3: View
    private lateinit var partyElement4: View

    private lateinit var gameLogic: EasyGameLogic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater doInflate (R.layout.easy_party to container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val partyNumber = partyNumberContainer.partyNumber
        gameLogic = EasyGameLogic(partyNumber)
        view.doInside {
            partyElement1 = findViewById(R.id.partyElement1)
            partyElement2 = findViewById(R.id.partyElement2)
            partyElement3 = findViewById(R.id.partyElement3)
            partyElement4 = findViewById(R.id.partyElement4)
            findViewById<View>(R.id.backButton).setOnClickListener {
                if(parentFragmentManager.fragments.size == 3) {
                    PossibleDestinations.POP_BACK_STACK navigate null
                }
            }
        }

        partyElement1.setOnClickListener {
            partyElementClick(it, 0)
        }
        partyElement2.setOnClickListener {
            partyElementClick(it, 1)
        }
        partyElement3.setOnClickListener {
            partyElementClick(it, 2)
        }
        partyElement4.setOnClickListener {
            partyElementClick(it, 3)
        }

        gameLogic.canClick.observe(viewLifecycleOwner) {
            partyElement1.isEnabled = it
            partyElement2.isEnabled = it
            partyElement3.isEnabled = it
            partyElement4.isEnabled = it
        }
        gameLogic.timer.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.partyTime).text = getString(R.string.party_time, it / 60, it % 60)
            if(it == 0) {
                lose()
            }
        }
        gameLogic.picked.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.partyProgress).text = getString(R.string.easy_party_progress, it)
        }
        beginGame()
    }

    private fun beginGame() {
        view?.doInside {
            findViewById<View>(R.id.backButton).isEnabled = true
            gameLogic.canClick.valueNotNull = false
            val partyNumber = partyNumberContainer.partyNumber
            gameLogic.stopTimer = false
            gameLogic.timer.valueNotNull = gameLogic.timer.default
            gameLogic.pickedPartiesStack.clear()
            gameLogic.picked.valueNotNull = 0

            partyElement1.setBackgroundResource(R.drawable.element_hidden)
            partyElement2.setBackgroundResource(R.drawable.element_hidden)
            partyElement3.setBackgroundResource(R.drawable.element_hidden)
            partyElement4.setBackgroundResource(R.drawable.element_hidden)

            findViewById<TextView>(R.id.partyNumber).text = getString(R.string.party_number, partyNumber)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            gameLogic.startGame(listOf(partyElement1, partyElement2, partyElement3, partyElement4))
        }
    }


    private fun failed() {
        viewLifecycleOwner.lifecycleScope.launch {
            gameLogic.canClick.valueNotNull = false
            delay(1000)

            gameLogic.picked.valueNotNull = 0
            gameLogic.pickedPartiesStack.clear()
            partyElement1.setBackgroundResource(R.drawable.element_hidden)
            partyElement2.setBackgroundResource(R.drawable.element_hidden)
            partyElement3.setBackgroundResource(R.drawable.element_hidden)
            partyElement4.setBackgroundResource(R.drawable.element_hidden)
            gameLogic.canClick.valueNotNull = true
        }
    }

    private fun partyElementClick(partyElement: View, partyElementNumber: Int) {
        val result = gameLogic.pickParty(partyElementNumber)
        if(result != 3) {
            partyElement.setBackgroundResource(R.drawable.element_01 + gameLogic.partyOrder[partyElementNumber])
        }
        if(result == 1) {
            gameLogic.picked.valueNotNull += 1
        }
        if(result == 2) {
            return
        }
        when(result) {
            0 -> failed()
            3 -> win()
        }
    }

    private fun win() {
        view?.doInside {
            gameLogic.canClick.valueNotNull = false
            val partyNumber = partyNumberContainer.partyNumber
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                if(partyNumber != 20) {
                    partiesDatabase.getDao().partyUnlocked(partyNumber + 1)
                }
            }
            findViewById<View>(R.id.backButton).isEnabled = false
            this as ViewGroup
            View.inflate(context, R.layout.party_good, this)
            findViewById<View>(R.id.nextButton).setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
                if(partyNumber < 6) {
                    PossibleDestinations.EASY_PARTY navigate partyNumber + 1
                }
                else {
                    PossibleDestinations.NORMAL_PARTY navigate 7
                }
            }
            findViewById<View>(R.id.replayButton).setOnClickListener {
                removeView(findViewById(R.id.goodParty))
                beginGame()
            }
            findViewById<View>(R.id.homeButton).setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
            }
        }
    }

    private fun lose() {
        view?.doInside {
            gameLogic.canClick.valueNotNull = false
            findViewById<View>(R.id.backButton).isEnabled = false
            this as ViewGroup
            View.inflate(context, R.layout.party_bad, this)
            findViewById<View>(R.id.replayButton).setOnClickListener {
                removeView(findViewById(R.id.badParty))
                beginGame()
            }
            findViewById<View>(R.id.homeButton).setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
            }
        }
    }
}