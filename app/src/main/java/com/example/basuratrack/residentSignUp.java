//package com.example.basuratrack;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class residentSignUp extends AppCompatActivity {
//
//    private TextInputEditText editTextFName, editTextMName, editTextLName, editTextContactN, editTextEmail, editTextPassword, editTextConfirmPass;
//    private FirebaseAuth mAuth;
//    private Button createAccountButton;
//    private ProgressBar progressBar;
//    private TextView backToLogIn, passwordMatchStatus;
//    private String lastValidatedEmail = "";
////    private Intent intent;
////    String lastValidatedFname = "", lastValidatedMname = "",
////            lastValidatedLname = "", lastValidatedContact= "",
////            lastValidatedEmail = "", lastValidatedPassword = "",
////            lastValidatedConfirmPass = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        initializeViews();
//        setupPasswordMatchWatcher();
//        setupButtonListener();
//
////        mAuth = FirebaseAuth.getInstance();
//
////        TextView passwordMatchStatus = findViewById(R.id.passwordMatchStatus);
////
////        createAccountButton = findViewById(R.id.btnCreateAccount);
////        progressBar = findViewById(R.id.progressBar);
////        backToLogIn = findViewById(R.id.logInHere);
////
////        backToLogIn.setOnClickListener(view -> {
////            startActivity(new Intent(getApplicationContext(), userLogin.class));
////            finish();
////        });
//    }
//
//    private void initializeViews() {
//        editTextFName = findViewById(R.id.FName);
//        editTextMName = findViewById(R.id.MName);
//        editTextLName = findViewById(R.id.LName);
//        editTextContactN = findViewById(R.id.input_phone_number);
//        editTextEmail = findViewById(R.id.email);
//        editTextPassword = findViewById(R.id.password);
//        editTextConfirmPass = findViewById(R.id.ConfirmPass);
//        createAccountButton = findViewById(R.id.btnCreateAccount);
//        progressBar = findViewById(R.id.progressBar);
//        backToLogIn = findViewById(R.id.logInHere);
//        passwordMatchStatus = findViewById(R.id.passwordMatchStatus);
//    }
//
//    private void setupPasswordMatchWatcher() {
//        TextWatcher passwordWatcher = new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String password = editTextPassword.getText().toString();
//                String confirm = editTextConfirmPass.getText().toString();
//
//                if (confirm.isEmpty()) {
//                    passwordMatchStatus.setVisibility(View.GONE);
//                } else if (password.equals(confirm)) {
//                    passwordMatchStatus.setText("✅ Passwords match");
//                    passwordMatchStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//                    passwordMatchStatus.setVisibility(View.VISIBLE);
//                } else {
//                    passwordMatchStatus.setText("❌ Passwords do not match");
//                    passwordMatchStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                    passwordMatchStatus.setVisibility(View.VISIBLE);
//                }
//            }
//        };
//
//        editTextPassword.addTextChangedListener(passwordWatcher);
//        editTextConfirmPass.addTextChangedListener(passwordWatcher);
//    }
//
//    public void setupButtonListener() {
//        createAccountButton.setOnClickListener(view -> {
//            String buttonText = createAccountButton.getText().toString();
//            String email = String.valueOf(editTextEmail.getText());
//
//            if (buttonText.equals("CREATE ACCOUNT")) {
//                handleCreateAccount();
//
//            } else if (buttonText.equals("CONTINUE")) {
//
//                if (!email.equals(lastValidatedEmail)) {
//                    Toast.makeText(this, "Email changed. Please register again.", Toast.LENGTH_SHORT).show();
//
//                    createAccountButton.setText("CREATE ACCOUNT");
//                    return;
//                }
//
//                handleContinue();
//                finish();
//            }
//        });
//    }
//
//    private void handleCreateAccount() {
//        String fname, mname, lname, contact, email, password, confirmPass;
//
//            fname = String.valueOf(editTextFName.getText());
//            mname = String.valueOf(editTextMName.getText());
//            lname = String.valueOf(editTextLName.getText());
//            contact = String.valueOf(editTextContactN.getText());
//            email = String.valueOf(editTextEmail.getText());
//            password = String.valueOf(editTextPassword.getText());
//            confirmPass = String.valueOf(editTextConfirmPass.getText());
//
//        if (TextUtils.isEmpty(fname)){
//                    Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if (TextUtils.isEmpty(lname)){
//                    Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if (TextUtils.isEmpty(contact)){
//                    Toast.makeText(this, "Contact number is required", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if (TextUtils.isEmpty(email)){
//                    Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                    return;
//                };
//
//
//                progressBar.setVisibility((View.VISIBLE));
//                createAccountButton.setEnabled((false));
//
//                userSignUp.userRegistration(
//                        this,
//                        fname,
//                        mname,
//                        lname,
//                        contact,
//                        email,
//                        password,
//                        confirmPass,
//                        "resident",
//                        progressBar,
//                        createAccountButton
//                );
//
//                lastValidatedEmail = email;
//    }
//
//    private void handleContinue() {
//        progressBar.setVisibility(View.VISIBLE);
//        createAccountButton.setEnabled(false);
//
//        userSignUp.checkEmailVerification(
//                this,
//                progressBar,
//                createAccountButton
//        );
//    }
//
//    public void onResume() {
//        super.onResume();
//
////        When returning to app, checks if the user might have verified email in background
//
//        if (createAccountButton.getText().toString().equals("CONTINUE")) {
//            userSignUp.checkEmailVerification(
//                    this,
//                    progressBar,
//                    createAccountButton
//            );
//        }
//    }
//}
