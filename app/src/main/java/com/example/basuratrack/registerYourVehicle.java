package com.example.basuratrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class registerYourVehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_your_vehicle);

        Button btnProceed = findViewById(R.id.btnProceedToCollectorHome);
        btnProceed.setOnClickListener(view -> {
            Intent intent = new Intent(this, collectorMain.class);
            startActivity(intent);
        });
    }
}