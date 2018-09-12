package com.ebay.manualapp

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*
import android.animation.ValueAnimator



class DetailActivity : Activity() {
    var bitmap = Bitmap.createBitmap(176, 176, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    var cpaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        cpaint.color = Color.WHITE
        cpaint.style = Paint.Style.STROKE
        canvas.drawCircle(88f, 88f, 86f, cpaint)

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
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

        val animator = ValueAnimator.ofInt(99, 12)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            tvRemainPercent.text = animation.animatedValue.toString()
            val ival = animation.animatedValue
            if (ival is Int) {
                bitmap.eraseColor(Color.TRANSPARENT)
                canvas.drawCircle(88f, 88f, 86f, cpaint)
                canvas.drawArc(2f, 2f, 174f, 174f, -90f, 360f * (ival.toFloat() / 100f), false, paint)
            }
        }
        animator.start()
    }

    fun makeAuctionUri(itemNo: String): Uri {
        return Uri.parse("http://gmkt.kr/" + itemNo)
    }
}
