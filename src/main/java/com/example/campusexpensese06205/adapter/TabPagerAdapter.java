package com.example.campusexpensese06205.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.campusexpensese06205.ExpenseFragment;
import com.example.campusexpensese06205.ExpensesFragment;
import com.example.campusexpensese06205.IncomeFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ExpenseFragment(); // Fragment cho tab "Chi tiêu"
            case 1:
                return new IncomeFragment();  // Fragment cho tab "Thu nhập"
            default:
                return new ExpenseFragment(); // Mặc định là "Chi tiêu"
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Có 2 tab: "Chi tiêu" và "Thu nhập"
    }
}
