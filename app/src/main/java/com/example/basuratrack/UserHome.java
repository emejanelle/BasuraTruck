//package com.example.basuratrack;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class UserHome extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_user_home_page);
//
//    }
//}

package com.example.basuratrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basuratrack.databinding.ActivityMainBinding;

public class UserHome extends AppCompatActivity {
    ActivityMainBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the toolbar
        toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);

//        addBottomBorderToToolbar(toolbar);

        // Set up the toolbar for the default fragment (Home)
        setupToolbar("Home", false);

        // Remove default title for Home page
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set default fragment
        replaceFragment(new userHomePagefrgmnt());


        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                replaceFragment(new userHomePagefrgmnt());
                return true;
            } else if (item.getItemId() == R.id.transHistory) {
//                setupToolbar("", true);
                replaceFragment(new UserTransactHistoryfrgmnt());
                return true;
            } else if (item.getItemId() == R.id.profile) {
//                setupToolbar("", f);
                replaceFragment(new profilefrgmnt());
                return true;
            } else {
                return false;
            }

        });

    }

    private void replaceFragment(Fragment fragment) {
        Log.d("FragmentTransaction", "Replacing fragment: " + fragment.getClass().getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, fragment);
        fragmentTransaction.commit();
    }

    // Method to set up the toolbar dynamically
    public void setupToolbar(String title, boolean showBackButton) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);

            // Handle back button visibility
            if (showBackButton) {
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }
    private void addBottomBorderToToolbar(Toolbar toolbar) {
        // Create a View to act as the bottom border
        View border = new View(this);
        border.setLayoutParams(new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT // Match the toolbar width
                 // Height of the border in pixels (can use dp conversion for dynamic sizing)
        ));
        border.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set the border color

        // Add the border as a child view of the Toolbar
        toolbar.addView(border);

    }

}