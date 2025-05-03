package com.example.basuratrack;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userSignUp extends AppCompatActivity {
    public interface RegistrationCallback {
        void onSuccess(String role);
        void onFailure(String error);
    }

    public interface VerificationCallback {
        void onVerified(String role);
        void onNotVerified();
    }

    public static void userRegistration(Activity act, String fname, String mname, String lname, String contact, String email, String pass, String confirmPass, String role, ProgressBar progressBar, RegistrationCallback callback) {

//        Validation checks
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
                              callback.onSuccess(role);
                              Toast.makeText(act, "Verification email sent. Please verify before continuing.", Toast.LENGTH_SHORT).show();
                          } else {
                              callback.onFailure("Failed to send verification email.");
                          }
                      });
                   }).addOnFailureListener(e -> {
                       Toast.makeText(act, "Failed to save user data :<", Toast.LENGTH_SHORT).show();
                   });
               }
           } else {
               callback.onFailure("Authentication failed: " + task.getException().getMessage());
           }
        });
    }

    public static void checkEmailVerification(Activity activity, VerificationCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            callback.onNotVerified();
            return;
        }

        user.reload().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (user.isEmailVerified()) {

//                  Get user role
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String role = documentSnapshot.getString("role");

                                    callback.onVerified(role);
                                } else {
                                    callback.onNotVerified();
                                }
                            });
                } else {
                    callback.onNotVerified();
                }
            } else {
                callback.onNotVerified();
            }
        });
    }
}
