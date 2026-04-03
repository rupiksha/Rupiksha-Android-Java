package com.app.rupiksha.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.view.View
import java.io.FileOutputStream

class ViewPrintAdapter(private val context: Context, private val view: View) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        val info = PrintDocumentInfo.Builder("receipt.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .build()

        callback?.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        try {
            val document = PdfDocument()

            val width = view.width
            val height = view.height

            val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()

            val page = document.startPage(pageInfo)

            val canvas: Canvas = page.canvas
            view.draw(canvas)

            document.finishPage(page)

            destination?.fileDescriptor?.let {
                val out = FileOutputStream(it)
                document.writeTo(out)
                document.close()
                out.close()
            }

            callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))

        } catch (e: Exception) {
            callback?.onWriteFailed(e.toString())
        }
    }
}
