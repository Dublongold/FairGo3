package haveFun.inThis.onlineGame.goFair.tools

import android.graphics.Color
import android.view.Window
import android.view.WindowManager

class MainWindowManager(private val window: Window) {

    fun setAllTransparent() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

    fun setNoLimit() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}