package com.gamesoffair.gocsin.app.parts.iSayDisco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.extentions.doInflate
import com.gamesoffair.gocsin.app.extentions.doInside
import com.gamesoffair.gocsin.app.tools.PossibleDestinations

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