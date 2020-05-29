package com.example.christianbooklibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    private Button btnClickHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks
        btnClickHere = findViewById(R.id.btnClickHere);
        btnClickHere.setPaintFlags(btnClickHere.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Onclick listener for click here button
        btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToSignIn = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(backToSignIn);
                SignUpActivity.this.finish();
            }
        });
    }
}