package com.mit.harshada_gujar_chefbot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvSignIn, tvSignupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User already logged in, go to HomeActivity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignupLink = findViewById(R.id.tvSignupLink);

        // Login button click
        btnLogin.setOnClickListener(v -> login());

        // Tab click: Go to Sign Up page
        tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));

        // Tab click: Already on Sign In page
        tvSignIn.setOnClickListener(v ->
                Toast.makeText(this, "You are on Sign In page", Toast.LENGTH_SHORT).show());

        // Bottom link: Go to Sign Up page
        tvSignupLink.setOnClickListener(v ->
                startActivity(new Intent(this, SignupActivity.class)));
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Email & Password required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase login
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful, open HomeActivity
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Login failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
