package com.gamesoffair.gocsin.app.parts.iSayDisco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.database.PartiesDatabase
import com.gamesoffair.gocsin.app.extentions.DoWhenPopBackStack
import com.gamesoffair.gocsin.app.extentions.doInflate
import com.gamesoffair.gocsin.app.extentions.doInside
import com.gamesoffair.gocsin.app.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ChooseParty: Fragment(), DoWhenPopBackStack {

    private val partyDatabase: PartiesDatabase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater doInflate (R.layout.choose_party to container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doInside {
            synchronizeData()
            findViewById<View>(R.id.backButton).setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
            }
        }
    }

    private suspend fun getWhichPartiesCompleted(): List<Boolean> {
        return partyDatabase.getDao().getParties().map { it.unlocked == 1 }
    }

    private fun synchronizeData() {
        view?.doInside {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val witchPartiesCompleted = getWhichPartiesCompleted()
                for ((index, party) in allViews.filter { it.tag == "party" }.map { it as TextView }
                    .toList().withIndex()) {
                    handler.post {
                        if (witchPartiesCompleted[index]) {
                            val text = (index + 1).toString()
                            party.text = text
                            party.setOnClickListener {
                                if (parentFragmentManager.fragments.size == 2) {
                                    val partyNumber = index + 1
                                    when (partyNumber) {
                                        in 1..6 -> PossibleDestinations.EASY_PARTY
                                        in 7..14 -> PossibleDestinations.NORMAL_PARTY
                                        in 15..20 -> PossibleDestinations.HARD_PARTY
                                        else -> throw IllegalArgumentException()
                                    } navigate partyNumber
                                }
                            }
                            party.setBackgroundResource(R.drawable.level_cell)
                        } else {
                            party.setBackgroundResource(R.drawable.locked_level)
                            party.isClickable = false
                            party.isFocusable = false
                        }
                    }
                }
            }
        }
    }

    override fun doWhenPopBackStack() {
        synchronizeData()
    }
}