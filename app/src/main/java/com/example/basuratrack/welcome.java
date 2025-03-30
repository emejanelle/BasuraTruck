package com.example.basuratrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button learnMoreButton = findViewById(R.id.btnLearnMore);
        learnMoreButton.setOnClickListener(v -> {
//            Start of intro.xml act
            Intent intent = new Intent(welcome.this, IntroActivity.class);
            startActivity(intent);
        });

        Button getStartedBtn = findViewById(R.id.btnGetStarted);
        getStartedBtn.setOnClickListener(v -> {
//            Start of intro.xml act
            Intent intent = new Intent(welcome.this, UserHome.class);
            startActivity(intent);
        });


    }
}