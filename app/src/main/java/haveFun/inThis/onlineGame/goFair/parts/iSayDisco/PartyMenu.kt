package haveFun.inThis.onlineGame.goFair.parts.iSayDisco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.database.PartiesDatabase
import haveFun.inThis.onlineGame.goFair.extentions.MutableLiveDataWithDefault
import haveFun.inThis.onlineGame.goFair.extentions.doInflate
import haveFun.inThis.onlineGame.goFair.extentions.doInside
import haveFun.inThis.onlineGame.goFair.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PartyMenu: Fragment() {

    private val partiesDatabase: PartiesDatabase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater doInflate (R.layout.party_menu to container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doInside {
            val choosePartyButton = findViewById<View>(R.id.choosePartyButton)
            choosePartyButton.setOnClickListener {
                if(parentFragmentManager.fragments.size == 1) {
                    PossibleDestinations.CHOOSE_PARTY navigate null
                }
            }
            val privacyPolicyButton = findViewById<View>(R.id.privacyPolicyButton)
            privacyPolicyButton.setOnClickListener {
                if(parentFragmentManager.fragments.size == 1) {
                    PossibleDestinations.PRIVACY_PARTY navigate null
                }
            }
            val helpButton = findViewById<View>(R.id.helpButton)
            helpButton.setOnClickListener {
                if(parentFragmentManager.fragments.size == 1) {
                    PossibleDestinations.HELP_FOR_PARTY navigate null
                }
            }
            val partyRewardButton = findViewById<View>(R.id.partyRewardButton)
            partyRewardButton.setOnClickListener {
                if(parentFragmentManager.fragments.size == 1) {
                    PossibleDestinations.PARTY_REWARD navigate null
                }
            }
        }
    }
}