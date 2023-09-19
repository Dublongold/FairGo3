package com.gamesoffair.gocsin.app.discoAndParty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.database.PartiesDatabase
import com.gamesoffair.gocsin.app.tools.MainFragmentManagerReceiver
import com.gamesoffair.gocsin.app.tools.MainWindowManager
import com.gamesoffair.gocsin.app.tools.PartyNumberContainer
import com.gamesoffair.gocsin.app.tools.PossibleDestinations
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ISayDisco : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OneSignal.initWithContext(applicationContext, getString(R.string.one_signal))
        lifecycleScope.launch(Dispatchers.IO) {
            OneSignal.Notifications.requestPermission(true)
        }
        val database = Room.databaseBuilder(this, PartiesDatabase::class.java, "parties").build()
        if(GlobalContext.getOrNull() == null) {
            startKoin {
                modules(
                    module {
                        single {
                            MainFragmentManagerReceiver().apply{
                                getFragmentManager = ::getSupportFragmentManager
                            }
                        }
                        single {
                            PartyNumberContainer()
                        }
                        single {
                            database
                        }
                    }
                )
            }
        }
        val mainWindowManager = MainWindowManager(window)
        mainWindowManager.setAllTransparent()
        mainWindowManager.setNoLimit()
        setContentView(R.layout.i_say_disco)
        PossibleDestinations.PREPARATION_FOR_PARTY navigate null
    }
}