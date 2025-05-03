package com.example.basuratrack;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class signUp extends AppCompatActivity {
    private TextInputEditText editTextFName, editTextMName, editTextLName,
            editTextContactN, editTextEmail, editTextPassword, editTextConfirmPass;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private TextView passwordMatchStatus;
    private String userRole; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get the selected role from intent
        userRole = getIntent().getStringExtra("USER_ROLE");
        if (userRole == null) {
            // Default to resident if no role provided
            userRole = "resident";
            Toast.makeText(this, "No role selected, defaulting to resident", Toast.LENGTH_SHORT).show();
        }

        initializeViews();
        setupPasswordMatchWatcher();
//        setupRoleSelection();
        setupButtonListener();
    }
    private void initializeViews() {
        editTextFName = findViewById(R.id.FName);
        editTextMName = findViewById(R.id.MName);
        editTextLName = findViewById(R.id.LName);
        editTextContactN = findViewById(R.id.input_phone_number);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPass = findViewById(R.id.ConfirmPass);
        createAccountButton = findViewById(R.id.btnCreateAccount);
        progressBar = findViewById(R.id.progressBar);
        passwordMatchStatus = findViewById(R.id.passwordMatchStatus);
    }

    private void setupPasswordMatchWatcher() {
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = editTextPassword.getText().toString();
                String confirm = editTextConfirmPass.getText().toString();

                if (confirm.isEmpty()) {
                    passwordMatchStatus.setVisibility(View.GONE);
                } else if (password.equals(confirm)) {
                    passwordMatchStatus.setText("✅ Passwords match");
                    passwordMatchStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    passwordMatchStatus.setVisibility(View.VISIBLE);
                } else {
                    passwordMatchStatus.setText("❌ Passwords do not match");
                    passwordMatchStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    passwordMatchStatus.setVisibility(View.VISIBLE);
                }
            }
        };

        editTextPassword.addTextChangedListener(passwordWatcher);
        editTextConfirmPass.addTextChangedListener(passwordWatcher);
    }

    private void setupButtonListener() {
        createAccountButton.setOnClickListener(view -> {
            String buttonText = createAccountButton.getText().toString();

            if (buttonText.equals("CREATE ACCOUNT")) {
                handleCreateAccount();
            } else if (buttonText.equals("CONTINUE")) {
                handleContinue();
            }
        });
    }

    private void handleCreateAccount() {
        String fname = editTextFName.getText().toString().trim();
        String mname = editTextMName.getText().toString().trim();
        String lname = editTextLName.getText().toString().trim();
        String contact = editTextContactN.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPass.getText().toString().trim();

        // Validation checks
        if (TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lname)) {
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            Toast.makeText(this, "Contact number is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        createAccountButton.setEnabled(false);

        userSignUp.userRegistration(
                this,
                fname,
                mname,
                lname,
                contact,
                email,
                password,
                confirmPass,
                userRole,
                progressBar,
                new userSignUp.RegistrationCallback() {
                    @Override
                    public void onSuccess(String role) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            createAccountButton.setText("CONTINUE");
                            createAccountButton.setEnabled(true);
                            Toast.makeText(signUp.this,
                                    "Verification email sent. Please verify your email.",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            createAccountButton.setEnabled(true);
                            Toast.makeText(signUp.this, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );
    }

    private void handleContinue() {
        progressBar.setVisibility(View.VISIBLE);
        createAccountButton.setEnabled(false);

        userSignUp.checkEmailVerification(
                this,
                new userSignUp.VerificationCallback() {
                    @Override
                    public void onVerified(String role) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);

                            Intent intent;
                            if ("resident".equals(role)) {
                                intent = new Intent(signUp.this, registerAddress.class);
                            } else if ("collector".equals(role)) {
                                intent = new Intent(signUp.this, registerYourVehicle.class);
                            } else {
                                Toast.makeText(signUp.this,
                                        "Unknown user role", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onNotVerified() {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            createAccountButton.setEnabled(true);
                            Toast.makeText(signUp.this,
                                    "Email not verified yet. Please check your inbox.",
                                    Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (createAccountButton.getText().toString().equals("CONTINUE")) {
            // Auto-check verification when returning to app
            handleContinue();
        }
    }

    @Override
    public void onBackPressed() {
        if (createAccountButton.getText().toString().equals("CONTINUE")) {
            Toast.makeText(this,
                    "Please complete email verification first",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}