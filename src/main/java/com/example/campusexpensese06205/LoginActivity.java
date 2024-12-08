package com.example.campusexpensese06205;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensese06205.database.UserDb;
import com.example.campusexpensese06205.model.UserModel;

public class LoginActivity extends AppCompatActivity {

    // Declare views
    private TextView tvRegister, tvForgetPassword;
    private EditText edtEmail, edtPass;
    private Button btnLogin, btnCancel;

    // Declare database helper
    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_login);

        // Initialize views
        tvRegister = findViewById(R.id.tvRegister);
        edtEmail = findViewById(R.id.editEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);

        // Initialize database helper
        userDb = new UserDb(this);

        // Set listeners for events
        setListeners();
    }

    // chuyển người dùng đến giao diện người dùng
    private void setListeners() {
        // Forget password click event
        tvForgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

        // Register click event
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Login button click event
        btnLogin.setOnClickListener(view -> loginWithDatabaseSQLite());

        // Cancel button click event
        btnCancel.setOnClickListener(view -> finish());  // Close the login activity
    }

    // lay du liẹu tu nguoi dung
    private void loginWithDatabaseSQLite() {
        // Get user inputs
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPass.setError("Password cannot be empty");
            return;
        }

        // Check login credentials in the database
        UserModel checkLogin = userDb.checkUserLogin(email, password);
        if (checkLogin != null) {
            // If login is successful, go to MenuActivity
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

            // Pass user data to the next activity
            Bundle bundle = new Bundle();
            bundle.putInt("ID_USER", checkLogin.getId());
            bundle.putString("USERNAME_USER", checkLogin.getUsername());
            intent.putExtras(bundle);

            // Start MenuActivity and finish LoginActivity
            startActivity(intent);
            finish();
        } else {
            // If login fails, show a toast message
            Toast.makeText(this, "Invalid account. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

}
