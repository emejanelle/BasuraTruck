package com.example.basuratrack;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class residentSignUp extends AppCompatActivity {

    private TextInputEditText editTextFName, editTextMName, editTextLName, editTextContactN, editTextEmail, editTextPassword, editTextConfirmPass;
    private FirebaseAuth mAuth;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private TextView backToLogIn;
    private String getLastValidatedEmail;
    private Intent intent;
    String lastValidatedFname = "", lastValidatedMname = "",
            lastValidatedLname = "", lastValidatedContact= "",
            lastValidatedEmail = "", lastValidatedPassword = "",
            lastValidatedConfirmPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editTextFName = findViewById(R.id.FName);
        editTextMName = findViewById(R.id.MName);
        editTextLName = findViewById(R.id.LName);
        editTextContactN = findViewById(R.id.input_phone_number);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPass = findViewById(R.id.ConfirmPass);

        TextView passwordMatchStatus = findViewById(R.id.passwordMatchStatus);

        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

                // Check for strong password criteria
//                if (password.length() < 8) {
//                    passwordMatchStatus.setText("❌ Password must be at least 8 characters");
//                    passwordMatchStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

// Attach to both fields to ensure re-checking
        editTextPassword.addTextChangedListener(passwordWatcher);
        editTextConfirmPass.addTextChangedListener(passwordWatcher);

        createAccountButton = findViewById(R.id.btnCreateAccount);
        progressBar = findViewById(R.id.progressBar);
        backToLogIn = findViewById(R.id.logInHere);

        backToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), loginSignup.class));
                finish();
            }
        });

        createAccountButton.setOnClickListener(view -> {
            String buttonText = createAccountButton.getText().toString();

            String fname, mname, lname, contact, email, password, confirmPass;

            fname = String.valueOf(editTextFName.getText());
            mname = String.valueOf(editTextMName.getText());
            lname = String.valueOf(editTextLName.getText());
            contact = String.valueOf(editTextContactN.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());
            confirmPass = String.valueOf(editTextConfirmPass.getText());
            if (buttonText.equals("CREATE ACCOUNT")) {
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(fname)){
                    Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(lname)){
                    Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(contact)){
                    Toast.makeText(this, "Contact number is required", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(confirmPass)) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                userSignUp.userRegistration(
                        this,
                        fname,
                        mname,
                        lname,
                        contact,
                        email,
                        password,
                        confirmPass,
                        "resident",
                        progressBar, createAccountButton);

                lastValidatedFname = fname;
                lastValidatedMname = mname;
                lastValidatedLname = lname;
                lastValidatedContact = contact;
                lastValidatedEmail = email;
                lastValidatedPassword = password;
                lastValidatedConfirmPass = confirmPass;

                createAccountButton.setText("CONTINUE");

            } else if (buttonText.equals("CONTINUE")) {

                // Check if the input has changed since last validation
                if (!fname.equals(lastValidatedFname) ||
                        !mname.equals(lastValidatedMname) ||
                        !lname.equals(lastValidatedLname) ||
                        !contact.equals(lastValidatedContact) ||
                        !email.equals(lastValidatedEmail) ||
                        !password.equals(lastValidatedPassword) ||
                        !confirmPass.equals(lastValidatedConfirmPass)) {

                    Toast.makeText(this, "Changes detected. Please tap 'Create Account' again to re-validate.", Toast.LENGTH_LONG).show();
                    createAccountButton.setText("CREATE ACCOUNT");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                userSignUp.checkEmailVerification(
                        this,
                        progressBar
                );
                finish();


            }

        });
    }
}
