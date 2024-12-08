package com.example.campusexpensese06205.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensese06205.model.BudgetModel;

import java.util.ArrayList;
import java.util.List;

public class   BudgetDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 2; // Tăng phiên bản để xử lý cập nhật

    // Tên bảng và cột
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ICON = "icon";
    private static final String COLUMN_DATE = "date"; // Thêm cột `date`

    // Câu lệnh tạo bảng
    private static final String CREATE_TABLE_BUDGET =
            "CREATE TABLE " + TABLE_BUDGET + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " INTEGER NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_ICON + " INTEGER NOT NULL, " +
                    COLUMN_DATE + " TEXT);"; // Thêm cột `date`

    public BudgetDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUDGET); // Tạo bảng
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_BUDGET + " ADD COLUMN " + COLUMN_DATE + " TEXT");
        }
    }

    // Thêm một Budget mới
    public long addBudget(BudgetModel budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, budget.getName());
        values.put(COLUMN_PRICE, budget.getPrice());
        values.put(COLUMN_DESCRIPTION, budget.getDescription());
        values.put(COLUMN_ICON, budget.getIcon());
        values.put(COLUMN_DATE, budget.getDate());

        long id = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return id;
    }

    // Lấy tất cả Budget
    public List<BudgetModel> getAllBudgets() {
        List<BudgetModel> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BudgetModel budget = new BudgetModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) // Thêm `date`
                );
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }

        if (cursor != null) cursor.close();
        db.close();
        return budgetList;
    }

    // Lấy một Budget theo ID
    public BudgetModel getBudgetById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            BudgetModel budget = new BudgetModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) // Thêm `date`
            );
            cursor.close();
            db.close();
            return budget;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    // Cập nhật Budget
    public boolean updateBudget(BudgetModel budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, budget.getName());
        values.put(COLUMN_PRICE, budget.getPrice());
        values.put(COLUMN_DESCRIPTION, budget.getDescription());
        values.put(COLUMN_DATE, budget.getDate()); // Sửa `date` đúng tên cột
        values.put(COLUMN_ICON, budget.getIcon());

        int rowsAffected = db.update(TABLE_BUDGET, values, COLUMN_ID + "=?", new String[]{String.valueOf(budget.getId())});
        db.close();
        return rowsAffected > 0;
    }

    // Xóa một Budget
    public int deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_BUDGET, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}
