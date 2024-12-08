package com.example.campusexpensese06205;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.campusexpensese06205.database.BudgetThuNhapDb;
import com.example.campusexpensese06205.database.IconDb;
import com.example.campusexpensese06205.model.BudgetModel;

import java.util.Calendar;

public class IncomeFragment extends Fragment {
    TextView tvDate;
    private EditText edSoTien,edGhiChu;

    private int selectedIconId = -1;
    private BudgetListAdapter adapter1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);

        BudgetThuNhapDb budgetDb = new BudgetThuNhapDb(getContext());

        adapter1 = new BudgetListAdapter(getContext(), budgetDb.getAllBudgets());


        // Khởi tạo RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.rcIncome);

        // Sử dụng GridLayoutManager với 4 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Khởi tạo Adapter với danh sách các icon chi tiêu và listener
        AdapterIcon adapter = new AdapterIcon(getContext(), IconDb.getThuNhapIcon(getContext()), iconId -> {
            selectedIconId = iconId;
            Log.d("ExpenseFragment", "Icon ID: " + iconId);
        });

        // Set adapter cho RecyclerView
        recyclerView.setAdapter(adapter);

        //khai báo
        tvDate = rootView.findViewById(R.id.tvDate);
        edSoTien = rootView.findViewById(R.id.edSoTien);
        edGhiChu = rootView.findViewById(R.id.edGhiChu);

        // Button lưu dữ liệu
        Button saveButton = rootView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(v -> {
            String soTienStr = edSoTien.getText().toString().trim();
            String ghiChu = edGhiChu.getText().toString().trim();
            String date = tvDate.getText().toString();

            if (selectedIconId == -1) {
                Toast.makeText(getContext(), "Vui lòng chọn một icon!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (soTienStr.isEmpty()) {
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
            BudgetModel newBudget = new BudgetModel(0, "Thu Nhap", soTien, ghiChu, selectedIconId, date);
            long newId = budgetDb.addBudget(newBudget);

            if (newId != -1) {
                adapter1.addBudgetToList(newBudget);
                Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                // Reset form sau khi lưu
                edSoTien.setText("");
                edGhiChu.setText("");
                selectedIconId = -1;
            } else {
                Toast.makeText(getContext(), "Lưu thất bại, thử lại!", Toast.LENGTH_SHORT).show();
            }
        });


        //mặc định ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        tvDate.setText(selectedDate);


        //sự kiện ấn vào chọn ngày

        CardView cardView = rootView.findViewById(R.id.cdDate);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở dialog chọn ngày
                openDatePickerDialog();
            }
        });


        return rootView;  // Trả về view sau khi xử lý
    }


    private void openDatePickerDialog() {
        // Lấy ngày, tháng và năm hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Cập nhật ngày đã chọn vào TextView
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tvDate.setText(selectedDate);
            }
        }, year, month, dayOfMonth);

        // Hiển thị dialog
        datePickerDialog.show();
    }


}