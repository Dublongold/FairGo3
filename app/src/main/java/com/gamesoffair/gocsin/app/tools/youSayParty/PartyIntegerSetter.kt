package com.gamesoffair.gocsin.app.tools.youSayParty

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Need for set web view some integer properties.
 */
object PartyIntegerSetter {
    fun set(areYouReallySayParty: WebView?) {
        areYouReallySayParty!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        areYouReallySayParty.settings.cacheMode = WebSettings.LOAD_DEFAULT
        Log.i("Party integer setter", "Web party integers was set successful!")
    }
}