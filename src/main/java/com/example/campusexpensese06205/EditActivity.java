package com.example.campusexpensese06205;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensese06205.adapter.AdapterIcon;
import com.example.campusexpensese06205.adapter.BudgetListAdapter;
import com.example.campusexpensese06205.database.BudgetDb;
import com.example.campusexpensese06205.database.BudgetThuNhapDb;
import com.example.campusexpensese06205.database.IconDb;
import com.example.campusexpensese06205.model.BudgetModel;
import com.example.campusexpensese06205.model.Icon;

import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private TextView tvDate;
    private EditText edSoTien, edGhiChu;
    private int selectedIconId = -1; // ID của icon
    private int budgetId; // ID của budget đang chỉnh sửa
    private BudgetDb budgetDb;
    private BudgetThuNhapDb budgetDbTN;
    private RecyclerView recyclerView;
    private AdapterIcon adapterIcon;
    private List<Icon> iconList;
    private BudgetListAdapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Khởi tạo database
        budgetDb = new BudgetDb(this);
        budgetDbTN = new BudgetThuNhapDb(this);


        // Bây giờ bạn có thể gọi phương thức getAllBudgets()
        List<BudgetModel> allBudgets = budgetDb.getAllBudgets();
        adapter1 = new BudgetListAdapter(this, allBudgets);


        // Lấy danh sách icon
        iconList = IconDb.getChiTieuIcons(this); // Lấy danh sách icon từ IconDb

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.rvExpenseCategory);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Lấy dữ liệu iconId đã chọn từ Intent
        selectedIconId = getIntent().getIntExtra("icon", -1);

        // Khởi tạo Adapter với danh sách các icon chi tiêu và listener
        adapterIcon = new AdapterIcon(this, iconList, iconId -> {
            selectedIconId = iconId; // Lưu id icon đã chọn
            Log.d("ExpenseFragment", "Icon ID: " + iconId);
        });

        // Set adapter cho RecyclerView
        recyclerView.setAdapter(adapterIcon);

        // Cập nhật selectedPosition trong AdapterIcon nếu đã có iconId
        if (selectedIconId != -1) {
            updateSelectedPosition(selectedIconId);
        }


        // Ánh xạ view
        tvDate = findViewById(R.id.tvDate);
        edSoTien = findViewById(R.id.edSoTien);
        edGhiChu = findViewById(R.id.edGhiChu);
        Button saveButton = findViewById(R.id.btnSave);
        CardView cardView = findViewById(R.id.cdDate);


        // Lấy dữ liệu từ Intent
        budgetId = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        int price = getIntent().getIntExtra("amount", 0);
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        selectedIconId = getIntent().getIntExtra("icon", -1);

        // Gắn dữ liệu cũ lên giao diện
        if (budgetId != -1) {
            tvDate.setText(date);
            edSoTien.setText(String.valueOf(price));
            edGhiChu.setText(description);
        }

        // Mặc định ngày hiện tại nếu không có ngày cũ
        if (date == null || date.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            tvDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }

        // Sự kiện lưu
        saveButton.setOnClickListener(v -> {
            String soTienStr = edSoTien.getText().toString().trim();
            String ghiChu = edGhiChu.getText().toString().trim();
            String selectedDate = tvDate.getText().toString();

            if (selectedIconId == -1) {
                Toast.makeText(this, "Vui lòng chọn một icon!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soTienStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soTien;
            try {
                soTien = Integer.parseInt(soTienStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            BudgetModel updatedBudget = new BudgetModel(budgetId, name, soTien, ghiChu, selectedIconId, selectedDate);
            boolean isUpdated = false;

            if (updatedBudget.getName().equals("Chi tiêu")) {
                isUpdated = budgetDb.updateBudget(updatedBudget);  // Update trong BudgetDb
            } else {
                isUpdated = budgetDbTN.updateBudget(updatedBudget);  // Update trong BudgetThuNhapDb
            }

            if (isUpdated) {
                // Cập nhật thành công
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isUpdated", true);  // Gửi thông tin rằng dữ liệu đã được cập nhật
                setResult(RESULT_OK, resultIntent);  // Trả lại kết quả cho BudgetFragment
                finish();
            } else {
                // Thông báo thất bại
                Toast.makeText(this, "Cập nhật thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }




        });

        // Sự kiện chọn ngày
        cardView.setOnClickListener(v -> openDatePickerDialog());
    }

    private void openDatePickerDialog() {
        // Lấy ngày, tháng và năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, day) -> {
            // Cập nhật ngày đã chọn
            String selectedDate = day + "/" + (monthOfYear + 1) + "/" + year1;
            tvDate.setText(selectedDate);
        }, year, month, dayOfMonth);

        // Hiển thị dialog
        datePickerDialog.show();
    }
    // Cập nhật vị trí đã chọn trong AdapterIcon
    private void updateSelectedPosition(int selectedIconId) {
        for (int i = 0; i < iconList.size(); i++) {
            if (iconList.get(i).getIconId() == selectedIconId) {
                adapterIcon.setSelectedPosition(i); // Set selectedPosition trong AdapterIcon
                adapterIcon.notifyDataSetChanged(); // Cập nhật lại dữ liệu cho RecyclerView
                break;
            }
        }
    }

}
