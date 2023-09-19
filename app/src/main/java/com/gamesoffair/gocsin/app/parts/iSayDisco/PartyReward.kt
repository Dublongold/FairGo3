package com.gamesoffair.gocsin.app.parts.iSayDisco

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.database.PartiesDatabase
import com.gamesoffair.gocsin.app.extentions.MutableLiveDataWithDefault
import com.gamesoffair.gocsin.app.extentions.doInflate
import com.gamesoffair.gocsin.app.extentions.doInside
import com.gamesoffair.gocsin.app.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Calendar

class PartyReward: Fragment() {

    private val partiesDatabase: PartiesDatabase by inject()
    private val timer = MutableLiveDataWithDefault(0L, 0L)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater doInflate (R.layout.party_reward to container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doInside {
            setOnClickListener {
                PossibleDestinations.POP_BACK_STACK navigate null
            }
            viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO) {
                val openAt = partiesDatabase.getDao().getOpenAt().firstOrNull()?.openAt
                val currentTime = Calendar.getInstance().time.time
                if(openAt != null && openAt > currentTime) {
                    Log.i("Open at", openAt.toString())
                    view.handler.post {
                        timer.valueNotNull = openAt + DAY - currentTime
                        startTimer()
                    }
                }
            }
            timer.observe(viewLifecycleOwner) {
                val seconds = it / 1000
                findViewById<TextView>(R.id.rewardButton).apply {
                    text = if(it != 0L) {
                        getString(R.string.party_reward_time, if(seconds == (3600 * 24).toLong()) 24 else seconds / 3600 % 24, seconds / 60 % 60, seconds % 60)
                    }
                    else {
                        getString(R.string.party_reward_get)
                    }
                    isEnabled = it == 0L
                    setTextColor(if(it == 0L) resources.getColor(R.color.reward_enabled, null) else Color.WHITE)
                }
            }
            findViewById<TextView>(R.id.rewardButton).setOnClickListener {
                timer.valueNotNull = DAY
                viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO) {
                    partiesDatabase.getDao().updateOpenAt(DAY + Calendar.getInstance().time.time)
                    startTimer()
                }
            }
        }
    }

    private fun startTimer() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (timer.valueNotNull > 0) {
                delay(1000)
                if (!isDetached) {
                    if (timer.valueNotNull >= 1000) {
                        timer.valueNotNull -= 1000L
                    } else if (timer.valueNotNull > 0) {
                        timer.valueNotNull = 0
                    }
                    else {
                        break
                    }
                    Log.i("Test", timer.valueNotNull.toString())
                }
            }
        }
    }

    companion object {
        const val DAY: Long = 24000L * 3600L
//        const val DAY: Long = 10000L
    }
}