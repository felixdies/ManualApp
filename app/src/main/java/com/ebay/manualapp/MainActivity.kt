package com.ebay.manualapp

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userItems: MutableList<Item> = mutableListOf()

    }

    class Item (val name: String, val img: String, val remain: Int)
}
