package com.example.tadawl.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tadawl.Utils.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = SharedPrefManager.getInstance(getApplicationContext()).GetToken();
        if (!token.equals("")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        finish();
    }
}
