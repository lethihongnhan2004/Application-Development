package com.example.campusexpensese06205;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvAmount, tvDescription;
    private Button btnEdit, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);  // Đảm bảo layout sử dụng đúng tên của layout cho Activity

        // Khởi tạo các view
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvAmount = findViewById(R.id.tvAmount);
        tvDescription = findViewById(R.id.tvDescription);
        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel); // Nút Cancel

        // Lấy dữ liệu từ Intent
        if (getIntent() != null) {
            tvId.setText(String.valueOf(getIntent().getIntExtra("id", -1)));
            tvName.setText(getIntent().getStringExtra("name"));
            tvAmount.setText(String.valueOf(getIntent().getIntExtra("amount", 0)));
            tvDescription.setText(getIntent().getStringExtra("description"));
        }

        // Hành động khi nhấn nút Edit
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(DetailActivity.this, "Edit clicked", Toast.LENGTH_SHORT).show();
        });


        // Hành động khi nhấn nút Cancel
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(DetailActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
        });
    }
}
