package com.example.christianbooklibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

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

        if (getItem.equals("Deeper Shopping")) {
            localPdfView.fromAsset("DeeperShopping Christian Books_Dr. Myles Munroe.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }

        if (getItem.equals("Rediscovering The Kingdom")) {
            localPdfView.fromAsset("Rediscovering The Kingdom.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }

        if (getItem.equals("Simply Christian")) {
            localPdfView.fromAsset("Simply Christian_ Why Christianity Makes Sense.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }

        if (getItem.equals("Wealth Without Theft")) {
            localPdfView.fromAsset("Wealth Without Theft_ You can be Rich without Stealing.pdf")
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        }
    }

    //The back button has to be clicked twice before it exits
    private long onBackPressTime;
    private Toast backToast;
    @Override
    public void onBackPressed() {
        if (onBackPressTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            backToast.cancel();
            return;
        }
        else{
            backToast = Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        onBackPressTime = System.currentTimeMillis();
    }
}