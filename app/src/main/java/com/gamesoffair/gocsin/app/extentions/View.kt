package com.gamesoffair.gocsin.app.extentions

import android.view.View

fun View.doInside(action: View.() -> Unit) {
    action()
}