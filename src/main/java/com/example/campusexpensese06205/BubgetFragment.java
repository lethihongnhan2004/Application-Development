package com.example.campusexpensese06205;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.campusexpensese06205.adapter.BudgetListAdapter;
import com.example.campusexpensese06205.database.BudgetDb;
import com.example.campusexpensese06205.database.BudgetThuNhapDb;
import com.example.campusexpensese06205.model.BudgetModel;

import java.util.ArrayList;
import java.util.List;
public class BubgetFragment extends Fragment {

    private ListView lvBudget;
    private TextView tvSpendingTab, tvCollectingTab;
    private List<BudgetModel> spendingBudgets;
    private List<BudgetModel> collectingBudgets;

    public BubgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bubget, container, false);

        lvBudget = view.findViewById(R.id.lvBudget);
        tvSpendingTab = view.findViewById(R.id.tvSpendingTab);
        tvCollectingTab = view.findViewById(R.id.tvCollectingTab);

        // Khởi tạo dữ liệu
        initializeData();

        // Thiết lập click listener cho các tab
        tvSpendingTab.setOnClickListener(v -> {
            // Đổi màu tab
            tvSpendingTab.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvCollectingTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            // Cập nhật danh sách cho tab chi tiêu
            updateBudgetList(spendingBudgets);
        });

        tvCollectingTab.setOnClickListener(v -> {
            // Đổi màu tab
            tvCollectingTab.setBackgroundColor(getResources().getColor(android.R.color.white));
            tvSpendingTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            // Cập nhật danh sách cho tab thu nhập
            updateBudgetList(collectingBudgets);
        });

        return view;
    }

    private void initializeData() {
        spendingBudgets = new ArrayList<>();  // Dành cho chi tiêu
        collectingBudgets = new ArrayList<>();  // Dành cho thu nhập

        // Khởi tạo đối tượng kết nối cơ sở dữ liệu
        BudgetDb budgetDb = new BudgetDb(requireContext());
        BudgetThuNhapDb budgetThuNhapDb = new BudgetThuNhapDb(requireContext());

        // Lấy dữ liệu cho chi tiêu và thu nhập
        spendingBudgets.addAll(budgetDb.getAllBudgets());  // Lấy chi tiêu
        collectingBudgets.addAll(budgetThuNhapDb.getAllBudgets());  // Lấy thu nhập
    }

    private void updateBudgetList(List<BudgetModel> budgets) {
        // Cập nhật lại ListView với dữ liệu mới
        BudgetListAdapter adapter = new BudgetListAdapter(getActivity(), budgets);
        lvBudget.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Tải lại dữ liệu từ database nếu cần
        loadDataFromDatabase();

        // Mặc định hiển thị tab chi tiêu
        tvSpendingTab.performClick();
    }

    private void loadDataFromDatabase() {
        // Khởi tạo các đối tượng kết nối cơ sở dữ liệu
        BudgetDb budgetDb = new BudgetDb(getContext());
        BudgetThuNhapDb budgetThuNhapDb = new BudgetThuNhapDb(getContext());

        // Lấy dữ liệu từ cả hai bảng
        spendingBudgets.clear();
        collectingBudgets.clear();
        spendingBudgets.addAll(budgetDb.getAllBudgets());
        collectingBudgets.addAll(budgetThuNhapDb.getAllBudgets());
    }

}