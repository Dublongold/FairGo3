package haveFun.inThis.onlineGame.goFair.discoAndParty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import haveFun.inThis.onlineGame.goFair.R
import haveFun.inThis.onlineGame.goFair.database.PartiesDatabase
import haveFun.inThis.onlineGame.goFair.tools.MainFragmentManagerReceiver
import haveFun.inThis.onlineGame.goFair.tools.MainWindowManager
import haveFun.inThis.onlineGame.goFair.tools.PartyNumberContainer
import haveFun.inThis.onlineGame.goFair.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ISayDisco : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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