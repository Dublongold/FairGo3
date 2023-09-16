package haveFun.inThis.onlineGame.goFair.tools.youSayParty

import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback

class WorkerWithFileForParty(private val fileForParty: ValueCallback<Array<Uri>>?, private val callbackForFileForParty: Uri?) {

    fun checkIfNull()= fileForParty == null

    fun checkResultCode(resultCode: Int): Boolean {
        return if(resultCode != -1) {
            fileForParty?.onReceiveValue(null)
            true
        }
        else {
            false
        }
    }

    fun checkData(data: Intent?): Boolean {
        return if(data == null) {
            sameCode()
            true
        }
        else {
            false
        }
    }

    fun checkDataString(dataString: String?): Boolean {
        return if(dataString == null) {
            sameCode()
            true
        }
        else {
            val uriArray = arrayOf(Uri.parse(dataString))
            fileForParty?.onReceiveValue(uriArray)
            false
        }
    }

    private fun sameCode() {
        fileForParty?.onReceiveValue(if (callbackForFileForParty != null) {
            arrayOf(callbackForFileForParty)
        } else {
            null
        })
    }
}