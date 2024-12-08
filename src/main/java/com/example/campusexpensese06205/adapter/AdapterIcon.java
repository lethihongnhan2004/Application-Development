package com.example.campusexpensese06205.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensese06205.R;
import com.example.campusexpensese06205.model.Icon;

import java.util.List;

public class AdapterIcon extends RecyclerView.Adapter<AdapterIcon.ViewHolder> {
    private Context context;
    private List<Icon> iconList;
    private int selectedPosition = -1; // Lưu vị trí item được chọn
    private OnIconSelectedListener listener; // Khai báo listener

    public AdapterIcon(Context context, List<Icon> iconList, OnIconSelectedListener listener) {
        this.context = context;
        this.iconList = iconList;
        this.listener = listener; // Khởi tạo listener
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icon, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Icon icon = iconList.get(position);

        // Cập nhật hình ảnh cho ImageView
        holder.iconImageView.setImageResource(icon.getIconId());

        // Thay đổi border nếu item được chọn
        if (position == selectedPosition) {
            holder.iconImageView.setSelected(true); // Thêm trạng thái selected cho item
        } else {
            holder.iconImageView.setSelected(false);
        }

        // Xử lý sự kiện click để thay đổi border
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;



            // Thông báo thay đổi
            notifyItemChanged(previousPosition);
            notifyItemChanged(position);
            // Truyền iconId cho Fragment
            if (listener != null) {
                listener.onIconSelected(icon.getIconId()); // Gọi phương thức từ listener
            }
            Toast.makeText(context,  icon.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.ivIcon);
        }
    }
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }


    public interface OnIconSelectedListener {
        void onIconSelected(int iconId);  // Tham số là id của icon
    }

}