package com.example.basuratrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
//        TabLayout tabLayout = findViewById(R.id.tabLayout);

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
        skipButton.setOnClickListener(v -> {
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
                    Toast.makeText(this, "residentCardView clicked!", Toast.LENGTH_SHORT).show();
                    // Proceed to the Resident layout
                    Intent intent = new Intent(this, residentSignUp.class);
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
                    // Proceed to the Resident layout
//                    Intent intent = new Intent(this, collectorSignUp.class); // Replace with your target activity class
//                    startActivity(intent);
                });


            } else {
                Toast.makeText(this, "Parent layout not found in dialog!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
