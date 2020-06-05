package com.example.christianbooklibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class OnlinePdfViewerActivity extends AppCompatActivity {

    //Variables for downloading pdf file to internal storage.
    private final String PDF_LINK = "https://drive.google.com/u/0/uc?id=1RroytkN7ybNor1I1k4lbFoYq61TRpyWZ&export=download";
    private final String MY_PDF = "book.pdf";
    private AppCompatSeekBar seekBar;
    private TextView textView;
    private PDFView onlinePdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pdf_viewer);

        //Hooks for downloading pdf file to internal storage.
        textView = findViewById(R.id.textSeekBar);
        onlinePdfView = findViewById(R.id.onlinePdfView);

        String getItem = getIntent().getStringExtra("onlinePdfFileName");

        //This is the book name as it appears on the list view
        if (getItem.equals("Government")) {
            initSeekBar();
            downloadPdf(MY_PDF);
        }
    }

    //This three methods initSeekBar, downloadPdf and openPdf gets the pfg
    //file direct from internet and saves to internal storage then displays to list view.
    private void initSeekBar() {
        seekBar = findViewById(R.id.seekBar);
        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth()) - 4
                        * seekBar.getThumbOffset()) / seekBar.getMax();
                textView.setText("" + progress);
                textView.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void downloadPdf(final String fileName) {
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return downloadPdf();
            }

            @Nullable
            private Boolean downloadPdf() {
                try {
                    File file = getFileStreamPath(fileName);
                    if (file.exists())
                        return true;

                    try {
                        /*----------------Function to exit of net is not connected-----------------*/
                        FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        URL u = new URL(PDF_LINK);
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();
                        InputStream input = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentLength];
                        long total = 0;
                        int count;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            publishProgress((int) ((total * 100) / contentLength));
                            fileOutputStream.write(data, 0, count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        input.close();
                        return true;

                    } catch (final Exception e) {
                        e.printStackTrace();
                        return false; //Swallow a 404
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                seekBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    openPdf(fileName);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to download file", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void openPdf(String fileName) {

        File file = getFileStreamPath(fileName);

        if (file.isFile()) {

            try {
                Log.e("file: ", "file: " + file.getAbsolutePath());
                seekBar.setVisibility(View.GONE);
                onlinePdfView.setVisibility(View.VISIBLE);
                onlinePdfView.fromFile(file)
                        .defaultPage(0)
                        .enableAnnotationRendering(true)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .spacing(2)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableAntialiasing(true)
                        .load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            initSeekBar();
            downloadPdf(MY_PDF);
        }
    }
}