package com.example.campusexpensese06205;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensese06205.database.UserDb;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText edtNewPassword, edtConfirmPassword;
    private Button btnUpdatePassword;
    private UserDb userDb;
    private String username = "";
    private String email = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        // Initialize views
        edtNewPassword = findViewById(R.id.edtNewpassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdate);

        // Initialize database helper
        userDb = new UserDb(this);

        // Get username and email from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("USERNAME_ACCOUNT", "");
            email = bundle.getString("EMAIL_ACCOUNT", "");

            // Validate if username or email is missing
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Invalid username or email. Please try again.", Toast.LENGTH_SHORT).show();
                finish(); // Close activity if invalid data
                return;
            }
        } else {
            Toast.makeText(this, "No user data provided.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no data
            return;
        }

        // Set button click listener
        setButtonClickListener();
    }

    private void setButtonClickListener() {
        btnUpdatePassword.setOnClickListener(view -> {
            // Get user input
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Validate new password
            if (TextUtils.isEmpty(newPassword)) {
                edtNewPassword.setError("New password cannot be empty");
                return;
            }

            // Validate confirm password
            if (TextUtils.isEmpty(confirmPassword)) {
                edtConfirmPassword.setError("Please confirm your password");
                return;
            }

            // Check if passwords match
            if (!confirmPassword.equals(newPassword)) {
                edtConfirmPassword.setError("Passwords do not match");
                return;
            }

            // Update password in the database
            int updateResult = userDb.updatePassword(newPassword, username, email);
            if (updateResult == -1) {
                Toast.makeText(UpdatePasswordActivity.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdatePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to login screen
                Intent intentLogin = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        });
    }
}
