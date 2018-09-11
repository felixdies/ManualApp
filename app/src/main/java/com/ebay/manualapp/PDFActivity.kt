package com.ebay.manualapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import kotlinx.android.synthetic.main.list_item.*
import java.io.File
import java.io.IOException

class PDFActivity : Activity() {
    var pdfPath: String = ""
    var curPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        var pdfName = intent.getStringExtra("pdfName")
        var pdfPage = intent.getIntExtra("pdfPage", 0)
        curPage = pdfPage
        pdfPath = Environment.getExternalStorageDirectory().toString() + "/Manuals/" + pdfName + ".pdf"

        try {
            openPDF(pdfPage)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this,
                    "Something Wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show()
        }

    }

    private fun openPDF(page: Int) {
        val file = File(pdfPath)

        var fileDescriptor: ParcelFileDescriptor? = null
        fileDescriptor = ParcelFileDescriptor.open(
                file, ParcelFileDescriptor.MODE_READ_ONLY)

        //min. API Level 21
        var pdfRenderer: PdfRenderer? = null
        pdfRenderer = PdfRenderer(fileDescriptor!!)

        val pageCount = pdfRenderer.pageCount
        Toast.makeText(this,
                "" + (page + 1) + "/" + pageCount,
                Toast.LENGTH_LONG).show()

        //Display page 0
        val rendererPage = pdfRenderer.openPage(page)
        val rendererPageWidth = rendererPage.width
        val rendererPageHeight = rendererPage.height
        val bitmap = Bitmap.createBitmap(
                rendererPageWidth,
                rendererPageHeight,
                Bitmap.Config.ARGB_8888)
        rendererPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        ivPdf.setImageBitmap(bitmap)
        rendererPage.close()

        pdfRenderer.close()
        fileDescriptor.close()
    }
}
