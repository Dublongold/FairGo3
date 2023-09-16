package haveFun.inThis.onlineGame.goFair.extentions

import android.view.View

fun View.doInside(action: View.() -> Unit) {
    action()
}