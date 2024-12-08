package com.example.campusexpensese06205;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusexpensese06205.adapter.TabPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView tabExpenses, tabIncome;
    private ViewPager2 viewPager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        Button clickMe = view.findViewById(R.id.btnClickMe);
//        clickMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
//            }
//        });
        //test
        // Ánh xạ các view
        tabExpenses = view.findViewById(R.id.tab_expenses);
        tabIncome = view.findViewById(R.id.tab_income);
        viewPager = view.findViewById(R.id.viewPager);

        // Thiết lập Adapter cho ViewPager2
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        // Thiết lập sự kiện click cho các TextView
        tabExpenses.setOnClickListener(v -> selectTab(0));
        tabIncome.setOnClickListener(v -> selectTab(1));

        // Đồng bộ ViewPager với TextView
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTabStyles(position);
            }
        });

        return view;
    }

    // Chuyển tab
    private void selectTab(int position) {
        viewPager.setCurrentItem(position, true);
    }

    // Cập nhật giao diện tab
    private void updateTabStyles(int position) {
        if (position == 0) {
            tabExpenses.setBackgroundResource(R.drawable.tab_selected_background);

            tabIncome.setBackgroundResource(R.drawable.tab_unselected_background);

        } else {
            tabIncome.setBackgroundResource(R.drawable.tab_selected_background);

            tabExpenses.setBackgroundResource(R.drawable.tab_unselected_background);

        }



    }


    ///test



}