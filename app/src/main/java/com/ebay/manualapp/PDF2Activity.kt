package com.ebay.manualapp

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_pdf2.*
import java.io.File

class PDF2Activity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf2)

        var pdfName = intent.getStringExtra("pdfName")
        var pdfPage = intent.getIntExtra("pdfPage", 0)

        val pdfPath = Environment.getExternalStorageDirectory().toString() + "/Manuals/" + pdfName + ".pdf"

        // /storage/emulated/0/Manuals
        pdfView.fromFile(File(pdfPath))
    }
}
