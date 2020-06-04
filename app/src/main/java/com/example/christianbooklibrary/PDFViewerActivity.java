package com.example.christianbooklibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PDFViewerActivity extends AppCompatActivity {

    PDFView localPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);

        localPdfView = findViewById(R.id.localPdfView);

        String getItem = getIntent().getStringExtra("pdfFileName");

        if (getItem.equals("How To Win Friends And Influence People")) {
            localPdfView.fromAsset("how_to_win_friends_and_influence_people.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }
    }
}