package com.example.basuratrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {
    Dialog dialog;
    Intent intent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        Button learnMoreButton = findViewById(R.id.btnLearnMore);
        learnMoreButton.setOnClickListener(v -> {
//            Start of intro.xml act
            Intent intent = new Intent(welcome.this, IntroActivity.class);
            startActivity(intent);
        });

        Button getStartedBtn = findViewById(R.id.btnGetStarted);
        getStartedBtn.setOnClickListener(v -> {

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.materialcardview);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();

            // Find the parent layout in the dialog
            LinearLayout parentLayout = dialog.findViewById(R.id.cardViewParent);
            TextView registerAsTextView = dialog.findViewById(R.id.dialog_register_as);
            if (registerAsTextView != null) {
                registerAsTextView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "TextView not found!", Toast.LENGTH_SHORT).show();
            }


            if (parentLayout != null) {
                LayoutInflater inflater = LayoutInflater.from(this);

                // Inflate and add "Resident" card from item_card.xml
                View residentCard = inflater.inflate(R.layout.item_card, parentLayout, false);
                TextView residentLabel = residentCard.findViewById(R.id.signUpLabel);
                ImageView residentImage = residentCard.findViewById(R.id.imageViewCardView);
                residentLabel.setText("Resident");
                residentImage.setVisibility(View.GONE); // Hide the image if needed
                parentLayout.addView(residentCard);

                residentCard.setOnClickListener(view -> {
                    Toast.makeText(this, "Registering as resident", Toast.LENGTH_SHORT).show();
                    // Proceed to the Resident layout
                    Intent intent = new Intent(this, userSignUp.class);
                    startActivity(intent);
                });


                // Inflate and add "Collector" card from item_card.xml
                View collectorCard = inflater.inflate(R.layout.item_card, parentLayout, false);
                TextView collectorLabel = collectorCard.findViewById(R.id.signUpLabel);
                ImageView collectorImage = collectorCard.findViewById(R.id.imageViewCardView);
                collectorLabel.setText("Collector");
                collectorImage.setVisibility(View.GONE); // Hide the image if needed
                parentLayout.addView(collectorCard);

                collectorCard.setOnClickListener(view -> {
                    Toast.makeText(this, "Registering as collector", Toast.LENGTH_SHORT).show();
                    // Proceed to the Resident layout
                    Intent intent = new Intent(this, userSignUp.class);
                    startActivity(intent);
                });

            } else {
                Toast.makeText(this, "Parent layout not found in dialog!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            intent = new Intent(getApplicationContext(), userMain.class);
            startActivity(intent);
            finish();
        }
    }
}