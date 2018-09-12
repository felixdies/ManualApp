package com.ebay.manualapp

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var bitmap = Bitmap.createBitmap(176, 176, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        var paint = Paint()

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(88f, 88f, 86f, paint)

        paint.strokeWidth = 4f
        canvas.drawArc(2f, 2f, 174f, 174f, -90f, 360f * 0.12f, false, paint)

        ivDetailRemain.setImageBitmap(bitmap)

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
