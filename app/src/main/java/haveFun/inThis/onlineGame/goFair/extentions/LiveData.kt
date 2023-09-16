package haveFun.inThis.onlineGame.goFair.extentions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableLiveDataWithDefault<T>(value: T, val default: T): MutableLiveData<T>(value) {
    var valueNotNull: T
        get() = value ?: default
        set(value) {
            this.value = value
        }
}

class LiveDataWithDefault<T>(value: T, private val default: T): LiveData<T>(value) {
    val valueNotNull
        get() = if(value == null) default else value
}