//package com.example.campusexpensese06205.adapter;
//
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.campusexpensese06205.R;
//import com.example.campusexpensese06205.model.BudgetModel;
//
//import java.util.List;
//
//public class BudgetListAdapter extends BaseAdapter {
//    List<BudgetModel> budgets;
//    public BudgetListAdapter(List<BudgetModel> model){
//        super();
//        budgets =model;
//    }
//    @Override
//    public int getCount() {
//        return budgets.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return budgets.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return budgets.get(position).id;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//       View budgetList;
//       if(convertView == null){
//           budgetList = View.inflate(parent.getContext(), R.layout.budget_listview,null);
//       }else{
//           budgetList =convertView;
//       }
//       BudgetModel budget = (BudgetModel) getItem(position);
//        ImageView icon= budgetList.findViewById(R.id.imgLogo);
//        TextView tvId =budgetList.findViewById(R.id.tvId);
//        TextView tvName = budgetList.findViewById(R.id.tvName);
//        TextView tvPrice = budgetList.findViewById(R.id.tvPrice);
//        TextView tvDes = budgetList.findViewById(R.id.tvDescription);
//
//        icon.setImageResource(budget.icon);
//        tvId.setText(String.valueOf(budget.id));
//        tvName.setText(budget.name);
//        tvPrice.setText(String.valueOf(budget.price));
//        tvDes.setText(budget.description);
//       return budgetList;
//    }
//}
package com.example.campusexpensese06205.adapter;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusexpensese06205.DetailActivity;
import com.example.campusexpensese06205.EditActivity;
import com.example.campusexpensese06205.EditThuNhap;
import com.example.campusexpensese06205.R;
import com.example.campusexpensese06205.database.BudgetDb;
import com.example.campusexpensese06205.database.BudgetThuNhapDb;
import com.example.campusexpensese06205.model.BudgetModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BudgetListAdapter extends BaseAdapter {
    private Context context;
    private List<BudgetModel> budgets;
    private BudgetDb budgetDb;
    private BudgetThuNhapDb budgetDbTN;



    // Modify constructor to accept both Context and List<BudgetModel>
    public BudgetListAdapter(Context context, List<BudgetModel> model) {
        this.context = context;
        this.budgets = model;

    }

    @Override
    public int getCount() {
        return budgets.size();
    }

    @Override
    public Object getItem(int position) {
        return budgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return budgets.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View budgetList;




        // Inflate layout if needed
        if (convertView == null) {
            budgetList = View.inflate(context, R.layout.budget_listview, null);
        } else {
            budgetList = convertView;
        }

        BudgetModel budget = (BudgetModel) getItem(position);

        ImageView icon = budgetList.findViewById(R.id.imgLogo);
        TextView tvId = budgetList.findViewById(R.id.tvId);
        TextView tvName = budgetList.findViewById(R.id.tvName);
        TextView tvDate = budgetList.findViewById(R.id.tvDate);
        TextView tvPrice = budgetList.findViewById(R.id.tvPrice);
        TextView tvDes = budgetList.findViewById(R.id.tvDescription);

        Button btnEdit = budgetList.findViewById(R.id.btnEdit);
        Button btnDelete = budgetList.findViewById(R.id.btnDelete);

        icon.setImageResource(budget.getIcon());
        tvId.setText(String.valueOf(budget.getId()));
        tvName.setText(budget.getName());
        tvPrice.setText(formatCurrency(budget.getPrice()));
        tvDes.setText(budget.getDescription());
        tvDate.setText(budget.getDate());

        // Khởi tạo BudgetDb nếu cần
        BudgetDb budgetDb = new BudgetDb(context);
        BudgetThuNhapDb budgetDbTN = new BudgetThuNhapDb(context);

        // Xử lý nút Edit
        btnEdit.setOnClickListener(v -> {
            Intent intent;
            if (budget.getName().equals("Chi tiêu")) {
                intent = new Intent(context, EditActivity.class);
            } else {
                intent = new Intent(context, EditThuNhap.class);
            }

            // Truyền dữ liệu
            intent.putExtra("id", budget.getId());
            intent.putExtra("date", budget.getDate());
            intent.putExtra("name", budget.getName());
            intent.putExtra("amount", budget.getPrice());
            intent.putExtra("description", budget.getDescription());
            intent.putExtra("icon", budget.getIcon());

            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 100); // Khởi chạy màn hình chỉnh sửa
            }

        });

        // Xử lý nút Delete
        btnDelete.setOnClickListener(v -> {
            int rowsDeleted;
            // Gọi phương thức xóa trong BudgetDb
            if (budget.getName().equals("Chi tiêu")) {
                 rowsDeleted = budgetDb.deleteBudget(budget.getId());
            }else {
                 rowsDeleted = budgetDbTN.deleteBudget(budget.getId());
            }
            if (rowsDeleted > 0) {
                // Nếu xóa thành công, xóa khỏi danh sách trong Adapter
                budgets.remove(position);  // Xóa item khỏi list
                notifyDataSetChanged();  // Cập nhật lại UI
                Toast.makeText(context, "Đã xóa mục", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
            }
        });

        return budgetList;
    }

    public void addBudgetToList(BudgetModel newBudget) {
        // Thêm mục mới vào danh sách
        budgets.add(newBudget);

        // Cập nhật RecyclerView
        notifyDataSetChanged();
    }
    public String formatCurrency(double amount) {
        // Sử dụng định dạng tiền tệ của Việt Nam
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }


}

