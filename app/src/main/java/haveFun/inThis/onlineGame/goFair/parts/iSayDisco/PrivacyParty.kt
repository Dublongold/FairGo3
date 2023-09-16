package haveFun.inThis.onlineGame.goFair.parts.iSayDisco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.extentions.doInflate
import haveFun.inThis.onlineGame.goFair.extentions.doInside
import haveFun.inThis.onlineGame.goFair.tools.PossibleDestinations

class PrivacyParty: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater doInflate (R.layout.privacy_party to container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doInside {
            findViewById<View>(R.id.backButton).setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
            }
        }
    }
}