package com.ebay.manualapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgSup1.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, makeAuctionUri(intent.getStringExtra("sup1"))))
        }

        imgSup2.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, makeAuctionUri(intent.getStringExtra("sup2"))))
        }

        imgSup3.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, makeAuctionUri(intent.getStringExtra("sup3"))))
        }
    }

    fun makeAuctionUri(itemNo: String): Uri {
        return Uri.parse("http://gmkt.kr/" + itemNo)
    }
}
