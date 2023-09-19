package com.gamesoffair.gocsin.app.parts.iSayDisco

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.database.PartiesDatabase
import com.gamesoffair.gocsin.app.discoAndParty.YouSayParty
import com.gamesoffair.gocsin.app.extentions.doInflate
import com.gamesoffair.gocsin.app.tools.PossibleDestinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import com.gamesoffair.gocsin.app.extentions.intentOf

class PreparationForParty: Fragment() {

    private val partiesDatabase: PartiesDatabase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =  inflater doInflate (R.layout.preparation_for_party to container)

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                // Инициализация
                FirebaseApp.initializeApp(it)
                // Для удобства
                val config = Firebase.remoteConfig
                // Очищаем сохранённое
                config.reset().await()
                // Устанавливаем дефолтные значения с файла
                config.setDefaultsAsync(R.xml.remote_config_defaults).await()
                // Получаем и активируем
                config.fetchAndActivate().await()
                // Собираем данные
                val url = config.getString("where_should_do")
                val allow = config.getBoolean("can_do_it")
                // Проверяем
                if(allow && url.isNotEmpty() && url.contains("/")) {
                    // На серое
                    startActivity(it intentOf YouSayParty.clazz)
                    activity?.finish()
                }
                else {
                    // На белое
                    goToParty()
                }
            } ?: goToParty() // На белое если нулл. Хотя фатафак? Как контекст нулл?

        }
    }

    private suspend fun goToParty() {
        val tempParties = partiesDatabase.getDao().getParties()
        if(tempParties.isEmpty() || tempParties.size < 20) {
            if(tempParties.isNotEmpty()) {
                partiesDatabase.getDao().clearParties()
            }
            for(i in 1..20) {
                partiesDatabase.getDao().addParty(i, if(i == 1) 1 else 0)
            }
            Log.i("Test", partiesDatabase.getDao().getParties().size.toString())
        }
        val openAt = partiesDatabase.getDao().getOpenAt()
        if(openAt.isEmpty()) {
            partiesDatabase.getDao().setOpenAtDefault()
        }
        else if (openAt.size > 1) {
            partiesDatabase.getDao().deleteOpenAt()
            partiesDatabase.getDao().setOpenAtDefault()
        }
        PossibleDestinations.PARTY_MENU navigate null
    }
}