package com.example.christianbooklibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {

    private Button signUp;
    private FloatingActionButton myFloLogin;
    private EditText emailId, password;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hooks
        signUp = findViewById(R.id.btnSignUp);
        myFloLogin = findViewById(R.id.btnFloatLogin);
        emailId = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        progressBar = new ProgressDialog(this);

        myFloLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionStatus();

                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (email.isEmpty()) {
                    emailId.setError("Cant be empty");
                    emailId.requestFocus();
                } else if (!email.matches(emailPattern)) {
                    emailId.setError("Invalid email");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                } else if (pwd.length() <= 5) {
                    password.setError("At least 6 characters");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {

                    progressBar.setMessage("Please waite...");
                    progressBar.show();
                }
            }
        });


        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //Underline sign up button text

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(sign);
                LoginActivity.this.finish();
            }
        });

    }

    //Check internet connection even when connected to network.
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    //Method checks the connection status
    private void connectionStatus() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED &&
                        (isOnline() == true)) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }

        if (connected == true){
            //Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();
        }
        else if (connected == false){

            new AlertDialog.Builder(this)
                    .setMessage("Please connect to the internet")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
