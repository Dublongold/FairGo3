package haveFun.inThis.onlineGame.goFair.tools.youSayParty

import android.webkit.WebView
import haveFun.inThis.onlineGame.goFair.extentions.doInside

/**
 * Need for set web view some boolean properties.
 */
class PartyBooleanSetter(private val areYouReallySayParty: WebView?) {
    fun setBadBooleans() {
        areYouReallySayParty?.doInside {
            this as WebView
            settings.javaScriptEnabled = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
        }
    }

    fun setEnabledOrAllowBooleans() {
        areYouReallySayParty?.doInside {
            this as WebView
            settings.databaseEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
        }
    }

    fun setOtherBooleans() {
        areYouReallySayParty?.doInside {
            this as WebView
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
        }
    }
}