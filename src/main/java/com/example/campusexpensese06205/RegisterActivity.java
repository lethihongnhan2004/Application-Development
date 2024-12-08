package com.example.campusexpensese06205;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensese06205.database.UserDb;

public class RegisterActivity extends AppCompatActivity {
    TextView tvLogin;
    EditText edtUser, edtPass, edtEmail, edtPhone, edtAddress;
    Button btnSignup;
    UserDb userDb;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_register);

        // Ánh xạ các thành phần giao diện
        tvLogin = findViewById(R.id.tvLogin);
        edtUser = findViewById(R.id.edtUsername);
        edtPass = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnSignup = findViewById(R.id.btnRegister);

        // Khởi tạo đối tượng UserDb
        userDb = new UserDb(RegisterActivity.this);

        // Xử lý sự kiện nhấn nút Đăng ký
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupWithDatabase();
            }
        });

        // Xử lý sự kiện chuyển sang màn hình Đăng nhập
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void signupWithDatabase() {
        // Lấy dữ liệu từ các trường nhập
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(user)) {
            edtUser.setError("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Password cannot be empty");
            return;
        }
        if (!isValidPassword(pass)) {
            edtPass.setError("Password must be at least 8 characters, including uppercase, lowercase, and numbers");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email cannot be empty");
            return;
        }
        if (!isValidEmail(email)) {
            edtEmail.setError("Invalid email format");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone cannot be empty");
            return;
        }
        if (!isValidPhone(phone)) {
            edtPhone.setError("Invalid phone number format");
            return;
        }

        // Lưu dữ liệu vào cơ sở dữ liệu
        long insert = userDb.addNewAccountUser(user, pass, email, phone, address);
        if (insert == -1) {
            // Thông báo nếu lưu không thành công
            Toast.makeText(this, "Username or Email already exists", Toast.LENGTH_SHORT).show();

        } else {
            // Thông báo nếu lưu thành công và chuyển sang màn hình Đăng nhập
            Toast.makeText(RegisterActivity.this, "Create Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //code validate
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
    private boolean isValidPassword(String password) {
        // Mật khẩu phải có ít nhất 8 ký tự, chứa chữ in hoa, chữ thường và số
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordPattern);
    }
    private boolean isValidPhone(String phone) {
        // Kiểm tra số điện thoại Việt Nam (10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)
        String phonePattern = "^(03|05|07|08|09)[0-9]{8}$";
        return phone.matches(phonePattern);
    }


}
