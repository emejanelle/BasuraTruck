//package com.example.basuratrack;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class collectorSignUp extends AppCompatActivity {
//
//    TextInputEditText editTextEmail, editTextPassword;
//    FirebaseAuth mAuth;
//    Button createAccountButton;
//    ProgressBar progressBar;
//    TextView backToLogIn;
//    Intent intent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        editTextEmail = findViewById(R.id.email);
//        editTextPassword = findViewById(R.id.password);
//        progressBar = findViewById(R.id.progressBar);
//        backToLogIn = findViewById(R.id.logInHere);
//        createAccountButton = findViewById(R.id.btnCreateAccount);
//
//        backToLogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent = new Intent(getApplicationContext(), loginSignup.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        TextInputEditText phoneInput = findViewById(R.id.input_phone_number); // Replace with your phone number input field ID
//
//// Hardcoded authentication code for static implementation
//        final String[] validAuthenticationCode = {"123456"}; // Initially hardcoded
//
//        createAccountButton.setOnClickListener(view -> {
//            String buttonText = createAccountButton.getText().toString();
//
//            if (buttonText.equals("CREATE ACCOUNT")) {
//                progressBar.setVisibility(View.VISIBLE);
//                String email, password;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//                if (TextUtils.isEmpty(email)){
//                    Toast.makeText(collectorSignUp.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(collectorSignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                userSignUp.userRegistration(
//                        collectorSignUp.this,
//                        email,
//                        password,
//                        "collector",
//                        progressBar);
//
//                createAccountButton.setText("CONTINUE");
//
//            } else if (buttonText.equals("CONTINUE")) {
//                userSignUp.checkEmailVerification(
//                        collectorSignUp.this,
//                        progressBar
//                );
//            }
//        });
//    }
//}