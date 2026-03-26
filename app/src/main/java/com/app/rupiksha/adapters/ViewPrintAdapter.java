package com.app.rupiksha.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.view.View;

import java.io.FileOutputStream;

public class ViewPrintAdapter extends PrintDocumentAdapter {

    private Context context;
    private View view;

    public ViewPrintAdapter(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle extras) {

        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("receipt.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .build();

        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages,
                        ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal,
                        WriteResultCallback callback) {

        try {
            PdfDocument document = new PdfDocument();

            int width = view.getWidth();
            int height = view.getHeight();

            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(width, height, 1).create();

            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            view.draw(canvas);

            document.finishPage(page);

            FileOutputStream out =
                    new FileOutputStream(destination.getFileDescriptor());

            document.writeTo(out);
            document.close();
            out.close();

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (Exception e) {
            callback.onWriteFailed(e.toString());
        }
    }
}
