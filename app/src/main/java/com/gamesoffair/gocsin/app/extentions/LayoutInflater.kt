package com.gamesoffair.gocsin.app.extentions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

infix fun LayoutInflater.doInflate(idToContainer: Pair<Int, ViewGroup?>): View? = inflate(idToContainer.first, idToContainer.second, false)