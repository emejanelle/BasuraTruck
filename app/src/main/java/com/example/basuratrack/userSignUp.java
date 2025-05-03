package com.example.basuratrack;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userSignUp {

    public static void userRegistration(Activity act, String fname, String mname, String lname, String contact, String email, String pass, String confirmPass, String role, ProgressBar progressBar, Button createAccountButton) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(act, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(act, task -> {
           progressBar.setVisibility(View.GONE);

           if (task.isSuccessful()) {
               FirebaseUser user = mAuth.getCurrentUser();

               if (user != null){
                   String uid = user.getUid();
                   FirebaseFirestore db = FirebaseFirestore.getInstance();

                   Map<String, Object> userMap = new HashMap<>();
                   userMap.put("role", role);
                   userMap.put("firstName", fname);
                   userMap.put("middleName", mname);
                   userMap.put("lastName", lname);
                   userMap.put("contact", contact);
                   userMap.put("email", email);
                   userMap.put("verified", false);

                   db.collection("users").document(uid).set(userMap).addOnCompleteListener(aVoid -> {
                      user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                          if (verifyTask.isSuccessful()){
                              Toast.makeText(act, "Verification email sent. Please verify before continuing.", Toast.LENGTH_LONG).show();

                          } else {
                              Toast.makeText(act, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                          }
                      });
                   }).addOnFailureListener(e -> {
                       Toast.makeText(act, "Failed to save user data :<", Toast.LENGTH_SHORT).show();
                   });
               }
           } else {
               Toast.makeText(act, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
           }
        });
    }

    public static void checkEmailVerification(Activity activity, ProgressBar progressBar) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(activity, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        user.reload().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                if (user.isEmailVerified()) {
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String role = documentSnapshot.getString("role");
                                    Intent intent;

                                    if ("collector".equals(role)) {
                                        intent = new Intent(activity, registerYourVehicle.class);
                                    } else if ("resident".equals(role)) {
                                        intent = new Intent(activity, registerAddress.class);
                                    } else {
                                        Toast.makeText(activity, "Unknown user role.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Toast.makeText(activity, "Email verified! Proceeding...", Toast.LENGTH_SHORT).show();
                                    activity.startActivity(intent);
                                    activity.finish();
                                } else {
                                    Toast.makeText(activity, "User role not found in Firestore.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(activity, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
                            });
//                    Intent intent = new Intent(activity, nextPageClass);
//                    activity.startActivity(intent);
//                    activity.finish();
                } else {
                    Toast.makeText(activity, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Failed to reload user.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
