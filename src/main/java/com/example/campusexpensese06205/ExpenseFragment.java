package com.example.campusexpensese06205;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusexpensese06205.adapter.AdapterIcon;
import com.example.campusexpensese06205.adapter.BudgetListAdapter;
import com.example.campusexpensese06205.database.BudgetDb;
import com.example.campusexpensese06205.database.IconDb;
import com.example.campusexpensese06205.model.BudgetModel;

import java.util.Calendar;

public class ExpenseFragment extends Fragment {

    TextView tvDate;
    private EditText edSoTien, edGhiChu;
    private int selectedIconId = -1;

    private BudgetListAdapter adapter1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        BudgetDb budgetDb = new BudgetDb(getContext());

        adapter1 = new BudgetListAdapter(getContext(), budgetDb.getAllBudgets());

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.rvExpenseCategory);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Khởi tạo Adapter với danh sách các icon chi tiêu và listener
        AdapterIcon adapter = new AdapterIcon(getContext(), IconDb.getChiTieuIcons(getContext()), iconId -> {
            selectedIconId = iconId;
            Log.d("ExpenseFragment", "Icon ID: " + iconId);
        });

        // Set adapter cho RecyclerView
        recyclerView.setAdapter(adapter);

        // Khai báo các thành phần
        tvDate = rootView.findViewById(R.id.tvDate);
        edSoTien = rootView.findViewById(R.id.edSoTien);
        edGhiChu = rootView.findViewById(R.id.edGhiChu);

        // Button lưu dữ liệu
        Button saveButton = rootView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(v -> {
            String soTienStr = edSoTien.getText().toString().trim();
            String ghiChu = edGhiChu.getText().toString().trim();
            String date = tvDate.getText().toString();

            // Kiểm tra nếu chưa chọn icon
            if (selectedIconId == -1) {
                Toast.makeText(getContext(), "Vui lòng chọn một icon!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra nếu số tiền chưa được nhập
            if (TextUtils.isEmpty(soTienStr)) {
                Toast.makeText(getContext(), "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soTien;
            try {
                soTien = Integer.parseInt(soTienStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu dữ liệu vào Database
            BudgetModel newBudget = new BudgetModel(0, "Chi tiêu", soTien, ghiChu, selectedIconId, date);
            long newId = budgetDb.addBudget(newBudget);

            if (newId != -1) {
                // Thêm mục mới vào Adapter và cập nhật RecyclerView
                adapter1.addBudgetToList(newBudget);
                Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                // Reset form sau khi lưu
                edSoTien.setText("");
                edGhiChu.setText("");
                selectedIconId = -1;
                tvDate.setText(getCurrentDate());  // Đặt lại ngày mặc định
            } else {
                Toast.makeText(getContext(), "Lưu thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        // Mặc định ngày hiện tại
        tvDate.setText(getCurrentDate());

        // Sự kiện ấn vào chọn ngày
        CardView cardView = rootView.findViewById(R.id.cdDate);
        cardView.setOnClickListener(v -> openDatePickerDialog());

        return rootView;
    }

    // Hàm trả về ngày hiện tại định dạng dd/MM/yyyy
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%02d/%02d/%d", dayOfMonth, month, year);
    }

    // Mở DatePickerDialog
    private void openDatePickerDialog() {
        // Lấy ngày, tháng và năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Cập nhật ngày đã chọn vào TextView
            String selectedDate = String.format("%02d/%02d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
            tvDate.setText(selectedDate);
        }, year, month, dayOfMonth);

        // Hiển thị dialog
        datePickerDialog.show();
    }
}
