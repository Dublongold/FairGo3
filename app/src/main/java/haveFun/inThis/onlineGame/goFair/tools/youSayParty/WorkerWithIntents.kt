package haveFun.inThis.onlineGame.goFair.tools.youSayParty

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.io.File

class WorkerWithIntents {
    fun createTakeJpgIntent(jpgFile: File?): Intent {
        val takeJpgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takeJpgIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(jpgFile))
        return takeJpgIntent
    }

    fun createPreviousIntent(type: String): Intent {
        val previous = Intent(Intent.ACTION_GET_CONTENT)
        previous.type = type
        previous.addCategory(Intent.CATEGORY_OPENABLE)
        return previous
    }

    fun createChooserIntentAndPutTwoExtra(
        previousIntent: Intent,
        intentForInitialIntents: Intent
    ): Intent {
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentForInitialIntents))
        chooser.putExtra(Intent.EXTRA_INTENT, previousIntent)
        return chooser
    }
}