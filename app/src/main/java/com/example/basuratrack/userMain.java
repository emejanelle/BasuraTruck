package com.example.basuratrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userMain extends AppCompatActivity {
    Button bookNowButton;
    Dialog dialog;
    private FrameLayout frameLayout;
    FirebaseAuth auth;
    TextView emailtxtView, nametxtView;
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


        loadUserHomePage();

//      onclick listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            home page
            if (item.getItemId() == R.id.home) {
                loadUserHomePage();
                setHeaderToolbarVisibility(true);
                return true;
            }

//            transaction history page
            else if (item.getItemId() == R.id.transHistory) {
                // Load Transaction History Page
                loadTransactionHistory();
                return true;
            }

//            profile page
            else if (item.getItemId() == R.id.profile) {
                loadProfile();
                return true;
            } else {
                return false;
            }
        });
    }

    private void loadUserHomePage() {
        frameLayout.removeAllViews(); // Clear previous content
        View userHomePageView = getLayoutInflater().inflate(R.layout.activity_user_home_f, null);
        frameLayout.addView(userHomePageView); // Load the new page

        bookNowButton = findViewById(R.id.book_now_button);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(userMain.this, "Book waste to be collected", Toast.LENGTH_SHORT).show();
                onBookNowButtonClicked();
            }
        });
    }

    private void loadTransactionHistory() {
        frameLayout.removeAllViews();
        View transHistoryView = getLayoutInflater().inflate(R.layout.activity_user_transaction, null);

        // Initialize AutoCompleteTextView for filter
        String[] items = {"All", "For Quotation", "Finished", "Cancelled"};
        AutoCompleteTextView autoCompleteTextView = transHistoryView.findViewById(R.id.auto_complete_txt);
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.list_items, items);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selectedItem = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(this, "Filtered: " + selectedItem, Toast.LENGTH_SHORT).show();
            // Add filtering logic here
        });

//        // Get data passed from BookSolidWaste (if available)
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        String contact = intent.getStringExtra("contact");
//        String location = intent.getStringExtra("location");
//        String status = intent.getStringExtra("status");
//        String transactionID = intent.getStringExtra("transactionID");
//        byte[] byteArray = intent.getByteArrayExtra("imageBitmap");
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        int placeholderImage = R.drawable.placeholder_image;  // Placeholder image
//
//        // Add the transaction to the list
//        List<UserTransaction> transactions = new ArrayList<>();
//        transactions.add(new UserTransaction(name, placeholderImage));
//
//        // Setup RecyclerView
//        RecyclerView recyclerView = transHistoryView.findViewById(R.id.recyclerViewerTransaction);
//        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

        frameLayout.addView(transHistoryView);
    }

    private void loadProfile() {
        frameLayout.removeAllViews();
        View profileView = getLayoutInflater().inflate(R.layout.activity_user_profile, null);
        frameLayout.addView(profileView);

        // Hide the header toolbar
        setHeaderToolbarVisibility(false);

        nametxtView = findViewById(R.id.profileNameTextview);
        emailtxtView = findViewById(R.id.emailView);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), userLogin.class);
            startActivity(intent);
            finish();
        } else {
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
    }

    private void onBookNowButtonClicked() {

//        FragmentManager fragmentManager = getChildFragmentManager();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.userbooknow_onclick);
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimationReport;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button bookNowCancel = dialog.findViewById(R.id.buttonBookNowCancel);
        bookNowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(userMain.this);
                builder.setTitle("Cancel Transaction");
                builder.setMessage("Are you sure you want to cancel this transaction?");
                builder.setPositiveButton("Yes", (alertDialog, which) -> {
                    Toast.makeText(userMain.this, "Transaction cancelled", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                builder.setNegativeButton("No", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        LinearLayout solidWasteLT = dialog.findViewById(R.id.bookSolidWaste);
        solidWasteLT.setOnClickListener(view -> {
            Intent intent = new Intent(this, bookSolidWaste.class);
//                intent.putExtra();
            startActivity(intent);
        });
//
        Log.d("userHomePagefrgmnt","Dialog opened successfully");
        dialog.show();
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