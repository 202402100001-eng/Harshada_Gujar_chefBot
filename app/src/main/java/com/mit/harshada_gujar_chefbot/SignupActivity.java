package com.mit.harshada_gujar_chefbot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLoginLink, tvSignIn, tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Sign out any previous user to avoid cached state
        mAuth.signOut();

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Button click
        btnSignup.setOnClickListener(v -> signup());

        // Top tabs
        tvSignIn.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class)));

        tvSignUp.setOnClickListener(v ->
                Toast.makeText(SignupActivity.this, "You are on Sign Up page", Toast.LENGTH_SHORT).show());

        // Bottom login link
        tvLoginLink.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void signup() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extra safety: sign out before signup
        mAuth.signOut();

        // Firebase Auth signup
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() || task.getException() instanceof FirebaseAuthUserCollisionException) {
                        // Treat collision as success since login works
                        saveUserToFirestore(email);
                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            Toast.makeText(this,
                                    "Signup error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void saveUserToFirestore(String email) {
        String uid = mAuth.getCurrentUser().getUid();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    // Navigate to HomeActivity only after successful signup save
                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignupActivity.this,
                            "Error saving user: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
    }
}
