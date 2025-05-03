package com.example.basuratrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class collectorMain extends AppCompatActivity {
    Intent intent;
    FrameLayout frameLayout;
    Button bookNowButton;
    Dialog dialog;
    FirebaseAuth auth;
    TextView emailtxtView;
    TextView btnSignout;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameLayout2);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        bottomNavigationView = findViewById(R.id.bottomNavBar);

        loadCollectorHomePage();

//      handler when user's navigate between pages using the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

//            collector home page
            if (item.getItemId() == R.id.home) {
                // Simulate rendering the CollectorHomePage within the frame
                loadCollectorHomePage();
                setHeaderToolbarVisibility(true);
                return true;
            }

//            transaction history
            else if (item.getItemId() == R.id.transHistory) {
                // Placeholder for Transaction History Page
                frameLayout.removeAllViews();
                View transHistoryView = getLayoutInflater().inflate(R.layout.activity_collector_trans_history, null);

                // Initialize AutoCompleteTextView for filter
                String[] items = {"All", "For Quotation", "Finished", "Cancelled"};
                AutoCompleteTextView autoCompleteTextView = transHistoryView.findViewById(R.id.auto_complete_txt);
                ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_items, items);
                autoCompleteTextView.setAdapter(adapterItems);

                // Set item click listener for filter
                autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                    String selectedItem = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(this, "Filtered: " + selectedItem, Toast.LENGTH_SHORT).show();
                    // Add filtering logic here to update the transaction list based on selection
                });

                frameLayout.addView(transHistoryView);
                setHeaderToolbarVisibility(true);
                return true;
            }

//            profile
            else if (item.getItemId() == R.id.profile) {
                frameLayout.removeAllViews();
                View profileView = getLayoutInflater().inflate(R.layout.activity_collector_profile, null);
                frameLayout.addView(profileView);

                // Hide the header toolbar
                setHeaderToolbarVisibility(false);

                emailtxtView = findViewById(R.id.emailView);
                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), userLogin.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    emailtxtView.setText(user.getEmail());
                }

                btnSignout = findViewById(R.id.btnTXTSignOut);
                btnSignout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(getApplicationContext(), userLogin.class);
                        startActivity(intent);
                        finish();

                    }
                });

                return true;
            } else {
                return false;
            }
        });
    }

//    method to call the collector's home page
    private void loadCollectorHomePage() {
        frameLayout.removeAllViews(); // Clear previous content
        View collectorHomePageView = getLayoutInflater().inflate(R.layout.activity_collector_home_page, null);
        frameLayout.addView(collectorHomePageView); // Load the new page
    }

    private void setHeaderToolbarVisibility(boolean isVisible) {
        androidx.appcompat.widget.Toolbar customToolbar = findViewById(R.id.customToolbar);
        if (customToolbar != null) {
            if (isVisible) {
                customToolbar.setVisibility(View.VISIBLE); // Show the toolbar
            } else {
                customToolbar.setVisibility(View.GONE); // Hide the toolbar
            }
        }
    }
}