package com.example.christianbooklibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PDFViewerActivity extends AppCompatActivity {

    PDFView myPdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);

        myPdfViewer = findViewById(R.id.pdfView);

        String getItem = getIntent().getStringExtra("pdfFileName");

        if (getItem.equals("Elly Wenani")){
            myPdfViewer.fromAsset("test.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }
    }
}