package com.ebay.manualapp

import java.util.Observable

class ObservableObject private constructor() : Observable() {
    fun updateValue(data: Any) {
        synchronized(this) {
            setChanged()
            notifyObservers(data)
        }
    }

    companion object {
        val instance = ObservableObject()
    }
}
