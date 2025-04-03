package com.example.basuratrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
            // Navigate to the main activity or dashboard
            Intent intent = new Intent(IntroActivity.this, UserHome.class);
            startActivity(intent);
            finish(); // Finish the IntroActivity
        });
    }
}
