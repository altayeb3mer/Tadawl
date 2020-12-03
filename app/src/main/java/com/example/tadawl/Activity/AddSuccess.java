package com.example.tadawl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tadawl.R;

public class AddSuccess extends AppCompatActivity {


    RelativeLayout relativeBackMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success);
        relativeBackMain = findViewById(R.id.relativeBackMain);
        relativeBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }


}