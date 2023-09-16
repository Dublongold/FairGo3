package haveFun.inThis.onlineGame.goFair.tools

import androidx.fragment.app.Fragment
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.extentions.DoWhenPopBackStack
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.ChooseParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.EasyParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.HardParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.HelpForParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.NormalParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.PartyMenu
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.PartyReward
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.PreparationForParty
import haveFun.inThis.onlineGame.goFair.parts.iSayDisco.PrivacyParty
import org.koin.java.KoinJavaComponent.inject

enum class PossibleDestinations {
    PREPARATION_FOR_PARTY,
    PARTY_MENU,
    HELP_FOR_PARTY,
    PARTY_REWARD,
    PRIVACY_PARTY,
    CHOOSE_PARTY,
    EASY_PARTY,
    NORMAL_PARTY,
    HARD_PARTY,
    POP_BACK_STACK;

    private val mainFragmentManagerReceiver: MainFragmentManagerReceiver by inject(MainFragmentManagerReceiver::class.java)
    private val partyNumberContainer: PartyNumberContainer by inject(PartyNumberContainer::class.java)

    infix fun navigate(data: Any?) {
        val fragmentManager = mainFragmentManagerReceiver.getFragmentManager()
        when(this) {
            PREPARATION_FOR_PARTY -> fragmentManager.beginTransaction()
                .add(R.id.partsContainer, getPartByDestination())
                .commit()
            HELP_FOR_PARTY, PARTY_REWARD, CHOOSE_PARTY, PRIVACY_PARTY -> fragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.partsContainer, getPartByDestination())
                .commit()
            PARTY_MENU -> {
                fragmentManager.beginTransaction()
                    .replace(R.id.partsContainer, getPartByDestination())
                    .commit()
            }
            EASY_PARTY, NORMAL_PARTY, HARD_PARTY -> {
                partyNumberContainer.partyNumber = data as Int
                fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.partsContainer, getPartByDestination())
                    .commit()
            }
            POP_BACK_STACK -> {
                fragmentManager.popBackStackImmediate()
                val lastPart = fragmentManager.fragments.last()
                if(lastPart is DoWhenPopBackStack) {
                    lastPart.doWhenPopBackStack()
                }
            }
        }
    }

    private fun getPartByDestination(): Fragment = when(this) {
        PREPARATION_FOR_PARTY -> PreparationForParty()
        PARTY_MENU -> PartyMenu()
        PARTY_REWARD -> PartyReward()
        HELP_FOR_PARTY -> HelpForParty()
        PRIVACY_PARTY -> PrivacyParty()
        CHOOSE_PARTY -> ChooseParty()
        EASY_PARTY -> EasyParty()
        NORMAL_PARTY -> NormalParty()
        HARD_PARTY -> HardParty()
        else -> throw IllegalArgumentException()
    }
}