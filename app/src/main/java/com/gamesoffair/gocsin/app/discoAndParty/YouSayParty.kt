package com.gamesoffair.gocsin.app.discoAndParty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.gamesoffair.gocsin.app.R
import com.gamesoffair.gocsin.app.tools.youSayParty.PartyBooleanSetter
import com.gamesoffair.gocsin.app.tools.youSayParty.PartyIntegerSetter
import com.gamesoffair.gocsin.app.tools.youSayParty.PartyWebClientAndWebChromeClient
import com.gamesoffair.gocsin.app.tools.youSayParty.WorkerWithFileForParty
import com.gamesoffair.gocsin.app.tools.youSayParty.WorkerWithIntents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class YouSayParty : AppCompatActivity() {
    var areYouReallySayParty: WebView? = null
    private var fileForParty: ValueCallback<Array<Uri>>? = null
    private var callbackForFileForParty: Uri? = null
    private lateinit var partyBooleanSetter: PartyBooleanSetter
    private lateinit var initialUrl: String

    fun onBackPressedCallbackObject(enabled: Boolean) = object: OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            val areYouReallySayPartyNotNull = areYouReallySayParty
            if(areYouReallySayPartyNotNull != null) {
                if(areYouReallySayPartyNotNull.canGoBack()) {
                    Log.i("On back pressed callback", "Go baaaack!")
                    areYouReallySayPartyNotNull.goBack()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.you_say_party)
        areYouReallySayParty = findViewById(R.id.areYouReallySayParty)
        partyBooleanSetter = PartyBooleanSetter(areYouReallySayParty)
        initialUrl = Firebase.remoteConfig.getString("where_should_do")
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().apply {
            Log.i("Cookie manager", "Accept cookie: ${acceptCookie()}")
            setAcceptThirdPartyCookies(areYouReallySayParty, acceptCookie())
        }
        preparationForParty()
        onBackPressedDispatcher.addCallback(onBackPressedCallbackObject(true))
        areYouReallySayParty!!.loadUrl(initialUrl)
    }

    private fun preparationForParty() {
        partyBooleanSetter.setBadBooleans()
        PartyIntegerSetter.set(areYouReallySayParty)
        areYouReallySayParty!!.settings.userAgentString = areYouReallySayParty!!.settings.userAgentString.replace("; wv", "")
        partyBooleanSetter.setEnabledOrAllowBooleans()
        partyBooleanSetter.setOtherBooleans()
        val partyWebClientAndWebChromeClient = PartyWebClientAndWebChromeClient()
        areYouReallySayParty!!.webChromeClient = partyWebClientAndWebChromeClient.buildWebChromeClient(requestPermissionLauncher) {
            fileForParty = it
        }
        areYouReallySayParty!!.webViewClient = partyWebClientAndWebChromeClient.buildWebClient()
    }

    val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        lifecycleScope.launch(Dispatchers.IO) {
            val jpgFile = createJpgTemporaryFile()
            val workerWithIntents = WorkerWithIntents()
            val takeJpgFileIntent = workerWithIntents.createTakeJpgIntent(jpgFile)
            callbackForFileForParty = Uri.fromFile(jpgFile)
            val previous = workerWithIntents.createPreviousIntent("*/*")
            val chooser = workerWithIntents.createChooserIntentAndPutTwoExtra(previous, takeJpgFileIntent)
            startActivityForResult(chooser, 1)
        }
    }

    private fun createJpgTemporaryFile() = try {
        File.createTempFile(
            "jpg_file",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    } catch (ex: IOException) {
        Log.e("Jpg file", "Unable to create jpg file, sorry.", ex)
        null
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val workerWithFileForParty = WorkerWithFileForParty(fileForParty, callbackForFileForParty)
        if(workerWithFileForParty.checkIfNull()) return
        if(!workerWithFileForParty.checkResultCode(resultCode)) {
            if (!workerWithFileForParty.checkData(data)) {
                Log.i("On activity result", "Do last branch with result ${workerWithFileForParty.checkDataString(data?.dataString)}")
            }
        }
        fileForParty = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val tempAreYouReallySayParty = areYouReallySayParty
        if(tempAreYouReallySayParty != null) {
            areYouReallySayParty!!.saveState(outState)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val tempAreYouReallySayParty = areYouReallySayParty
        if(tempAreYouReallySayParty != null) {
            areYouReallySayParty!!.restoreState(savedInstanceState)
        }
    }

    companion object {
        val clazz = YouSayParty::class.java
    }
}