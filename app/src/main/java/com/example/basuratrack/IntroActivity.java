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
import androidx.viewpager2.widget.ViewPager2;
import java.util.Arrays;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private LinearLayout indicatorLayout;
    private ImageView[] dots;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); // Inflate the XML layout

        ViewPager2 viewpager = findViewById(R.id.slideViewPager2);
        indicatorLayout = findViewById(R.id.indicator_layout);

        List<Integer> layoutIds = Arrays.asList(
                R.layout.fragment_user_about,
                R.layout.fragment_collector_about,
                R.layout.fragment_notice,
                R.layout.fragment_accepted_waste
        );

        // Set up the ViewPager2 adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, layoutIds);
        viewpager.setAdapter(adapter);

        // Create dots dynamically based on the number of pages in the ViewPager2
        int pageCount = adapter.getItemCount();
        dots = new ImageView[pageCount];
        for (int i = 0; i < pageCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot); // Inactive dot
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0); // Space between dots
            dots[i].setLayoutParams(params);
            indicatorLayout.addView(dots[i]);
        }

        // Set the first dot as selected (active dot)
        dots[0].setImageResource(R.drawable.selected_dot);

        // Register a page change callback to update dots on page change
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Loop through all dots and set the selected dot
                for (int i = 0; i < dots.length; i++) {
                    if (i == position) {
                        dots[i].setImageResource(R.drawable.selected_dot); // Active dot
                    } else {
                        dots[i].setImageResource(R.drawable.dot); // Inactive dot
                    }
                }
            }
        });

        // Set up the "SKIP" button functionality
        Button skipButton = findViewById(R.id.btnSkip);
        skipButton.setOnClickListener(v -> showRoleSelectionDialog());

    }

    private void showRoleSelectionDialog() {

        try {


            dialog = new Dialog(this);
            dialog.setContentView(R.layout.materialcardview);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);

            // Find the parent layout in the dialog
            LinearLayout parentLayout = dialog.findViewById(R.id.cardViewParent);
            TextView registerAsTextView = dialog.findViewById(R.id.dialog_register_as);
//
//            if (registerAsTextView != null) {
//                registerAsTextView.setVisibility(View.VISIBLE);
//            } else {
//                Toast.makeText(this, "TextView not found!", Toast.LENGTH_SHORT).show();
//            }
//
            if (parentLayout == null) {
                Toast.makeText(this, "Error loading selection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            parentLayout.removeAllViews();

            LayoutInflater inflater = LayoutInflater.from(this);
//
//                // Inflate and add "Resident" card from item_card.xml
            View residentCard = inflater.inflate(R.layout.item_card, parentLayout, false);
            TextView residentLabel = residentCard.findViewById(R.id.signUpLabel);
            ImageView residentImage = residentCard.findViewById(R.id.imageViewCardView);
            residentLabel.setText("Resident");
            residentImage.setVisibility(View.GONE); // Hide the image if needed
            parentLayout.addView(residentCard);

//                // Inflate and add "Collector" card from item_card.xml
            View collectorCard = inflater.inflate(R.layout.item_card, parentLayout, false);
            TextView collectorLabel = collectorCard.findViewById(R.id.signUpLabel);
            ImageView collectorImage = collectorCard.findViewById(R.id.imageViewCardView);
            collectorLabel.setText("Collector");
            collectorImage.setVisibility(View.GONE); // Hide the image if needed
            parentLayout.addView(collectorCard);

            residentCard.setOnClickListener(view -> {
                startSignUpActivity("resident");
                dialog.dismiss();
            });

            collectorCard.setOnClickListener(view -> {
                startSignUpActivity("collector");
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startSignUpActivity(String role) {
        Intent intent = new Intent(this, signUp.class);
        intent.putExtra("USER_ROLE", role);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }
}
