package com.example.christianbooklibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
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
    private TextView textView, textPleaseWait;
    private PDFView onlinePdfView;

    private ConstraintLayout myConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pdf_viewer);

        //Hooks for downloading pdf file to internal storage.
        textView = findViewById(R.id.textSeekBar);
        textPleaseWait = findViewById(R.id.textPleaseWaite);
        onlinePdfView = findViewById(R.id.onlinePdfView);
        myConstraintLayout = findViewById(R.id.myConstraintLayout);

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

                        /*----------------Function to exit if net is not connected-----------------*/
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
        try {
            final File file = getFileStreamPath(fileName);

            Log.e("file: ", "file: " + file.getAbsolutePath());
            seekBar.setVisibility(View.GONE);
            textPleaseWait.setVisibility(View.GONE);
            onlinePdfView.setVisibility(View.VISIBLE);
            onlinePdfView.fromFile(file)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAntialiasing(true)
                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Page errors!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            bookReDownload();
                            if (file.exists()){
                                file.delete();
                            }
                        }
                    })
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                            onlinePdfView.fitToWidth();
                        }
                    })
                    .load();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method deletes the book that did not finish downloading and re-downloads it again.
    private void bookReDownload(){
        new AlertDialog.Builder(this)
                .setMessage("The previous download was interrupted. Kindly " +
                        "press ok and open this book again to re-download!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        OnlinePdfViewerActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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