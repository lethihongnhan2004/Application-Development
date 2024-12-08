package com.example.campusexpensese06205.database;

import android.content.Context;

import com.example.campusexpensese06205.R;
import com.example.campusexpensese06205.model.Icon;

import java.util.ArrayList;
import java.util.List;

public class IconDb {
    // Tạo một danh sách các icon chi tiêu
    public static List<Icon> getChiTieuIcons(Context context) {
        List<Icon> iconList = new ArrayList<>();

        // Thêm các icon chi tiêu vào danh sách
        iconList.add(new Icon(R.drawable.trash_icon, "Rác"));
        iconList.add(new Icon(R.drawable.vehicle, "Đi Lại"));
        iconList.add(new Icon(R.drawable.house, "Tiền Nhà"));
        iconList.add(new Icon(R.drawable.medical, "Y Tế"));
        iconList.add(new Icon(R.drawable.entertain, "Tiền nước"));
        iconList.add(new Icon(R.drawable.meal, "Tiền Ăn"));
        iconList.add(new Icon(R.drawable.shopping, "Mua Sắm"));
        iconList.add(new Icon(R.drawable.other, "Khác"));
        return iconList;
    }
    public static List<Icon> getThuNhapIcon(Context context) {
        List<Icon> iconList = new ArrayList<>();

        // Thêm các icon chi tiêu vào danh sách
        iconList.add(new Icon(R.drawable.luong, "Lương"));
        iconList.add(new Icon(R.drawable.family, "Tiền Của Gia Đình"));
        iconList.add(new Icon(R.drawable.tienthuong, "Tiền Thưởng"));
        iconList.add(new Icon(R.drawable.invest, "Tiền Đầu Tư"));
        iconList.add(new Icon(R.drawable.other, "Khác"));
        return iconList;
    }
}
