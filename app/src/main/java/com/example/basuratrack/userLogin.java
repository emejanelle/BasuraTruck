package com.example.basuratrack;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class userLogin extends AppCompatActivity {
    Dialog dialog;
    TextView signUpButton;
    Button logInBtn;
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpHere);
        logInBtn = findViewById(R.id.btnLogIn);
        progressBar = findViewById(R.id.progressBar);

        //        log in button
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(userLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(userLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    String userID = user.getUid();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    db.collection("users").document(userID).get().addOnSuccessListener(documentSnapshot ->{
                                        if (documentSnapshot.exists()) {
                                            String role = documentSnapshot.getString("role");

                                            if ("resident".equals(role)) {
                                                intent = new Intent(userLogin.this, userMain.class);
                                                progressBar.setVisibility((View.GONE));
                                            } else if ("collector".equals(role)) {
                                                intent = new Intent(userLogin.this, collectorMain.class);
                                            } else {
                                                Toast.makeText(userLogin.this, "Role not found or invalid.", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility((View.GONE));
                                                return;
                                            }

                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(userLogin.this, "User data not found.", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(userLogin.this, "Failed to retrieve user role.", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Firestore error: ", e);
                                            });
                                }
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//                                    navigateBasedOnRole();
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Toast.makeText(userLogin.this, "Logged In Successfully!",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(userLogin.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
                            }
                        });
            }
        });

//        on user sign up
        signUpButton.setOnClickListener(view -> {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, welcome.class);
            startActivity(intent);
            finish();
        });
    }

    private void addCardToParent(View cardView) {
        // Find the parent container inside the MaterialCardView
        LinearLayout parentLayout = dialog.findViewById(R.id.cardViewParent);
        if (parentLayout != null) {
            parentLayout.addView(cardView); // Dynamically add the card
        } else {
            Toast.makeText(this, "Parent layout not found!", Toast.LENGTH_SHORT).show();
        }
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


    private void navigateBasedOnRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");

                        if ("resident".equals(role)) {
                            startActivity(new Intent(this, userMain.class));
                        } else if ("collector".equals(role)) {
                            startActivity(new Intent(this, collectorMain.class));
                        } else {
                            Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching user role.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error fetching role: ", e);
                });
    }


}