package haveFun.inThis.onlineGame.goFair.tools.youSayParty

import android.Manifest
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher

/**
 * Need for get web view client and web chrome client.
 */
class PartyWebClientAndWebChromeClient {
    fun buildWebClient() = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url.toString()
            return !(uri.contains("/") && uri.contains("http"))
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            Log.i("Do update visited history", "Do!")
            super.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            Log.i("Should override key event", "Key code: ${event?.keyCode}.")
            return super.shouldOverrideKeyEvent(view, event)
        }
    }

    fun buildWebChromeClient(requestPermissionLauncher: ActivityResultLauncher<String>, setNewParty: (ValueCallback<Array<Uri>>) -> Unit) = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            setNewParty(filePathCallback)
            return true
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.i("On console message", "Console message: ${consoleMessage?.message()}")
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            Log.i("On js confirm", "Result: $result")
            return super.onJsConfirm(view, url, message, result)
        }
    }
}