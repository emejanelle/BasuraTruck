package com.example.basuratrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro); // Inflate the XML layout

        // Initialize the ViewPager2 and LinearLayout
        ViewPager2 slideViewPager = findViewById(R.id.slideViewPager2);
        LinearLayout indicatorLayout = findViewById(R.id.indicator_layout);
//        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Setup ViewPager adapted
        ViewPagerAdapter adapter = new ViewPagerAdapter(this); // Attach adapter to ViewPager
        slideViewPager.setAdapter(adapter);

//        Create dots
        int pageCount = adapter.getItemCount();
        ImageView[] dots = new ImageView[pageCount];
        for (int i = 0; i < pageCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(8, 0, 8, 0);
        dots[i].setLayoutParams(params);
        indicatorLayout.addView(dots[i]);
        }

        dots[0].setImageResource(R.drawable.selected_dot);
        
        slideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        @Override
            public void onPageSelected (int position) {
                for(int i = 0; i < dots.length; i++) {
                    if (i == position) {
                        dots[i].setImageResource(R.drawable.selected_dot);
                    } else {
                        dots[i].setImageResource(R.drawable.dot);
                    }
                }
            }
        });
//        new TabLayout(tabLayout, slideViewPager, (tab, position) -> {
//            tab.setCustomView("");
//        }).attach();

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
