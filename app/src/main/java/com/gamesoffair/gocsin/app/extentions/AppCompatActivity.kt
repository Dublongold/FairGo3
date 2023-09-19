package com.gamesoffair.gocsin.app.extentions

import android.content.Context
import android.content.Intent

infix fun<T> Context.intentOf(clazz: Class<T>) = Intent(this, clazz)