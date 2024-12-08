package com.example.campusexpensese06205.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.campusexpensese06205.model.UserModel;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserDb extends SQLiteOpenHelper {
    public static final String DB_Name = "users_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";

    //khai bao ten cac cott trong bang du lieu
    public static final String ID_COL = "id";
    public static final String USERNAME_COL = "username";
    public static final String PASSWORD_COL = "password";
    public static final String EMAIL_COL = "email";
    public static final String PHONE_COL = "phone";
    public static final String ADDRESS_COL = "address";
    public static final String CREATED_COL = "created_at";
    public static final String UPDATED_COL = "updated_at";

    public UserDb(@Nullable Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //di tao bang du lieu vs SQLite
        String query = "CREATE TABLE " + TABLE_NAME + " ( " + ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_COL + " VARCHAR(60) NOT NULL, " + PASSWORD_COL + " VARCHAR(120) NOT NULL, " + EMAIL_COL + " VARCHAR(60) NOT NULL, " + PHONE_COL + " VARCHAR(20) NOT NULL, " + ADDRESS_COL + " TEXT, " + CREATED_COL + " DATETIME, " + UPDATED_COL + " DATETIME ) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long addNewAccountUser(String username, String password, String email, String phone, String address){

        if (checkUsernameOrEmailExists(username, email)) {
            return -1;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime now = ZonedDateTime.now();
        String currentDay = dtf.format(now);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        values.put(EMAIL_COL, email);
        values.put(PHONE_COL, phone);
        values.put(ADDRESS_COL, address);
        values.put(CREATED_COL, currentDay);
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert;
    }
    public UserModel checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            UserModel userModel = new UserModel();
            userModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)));
            userModel.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME_COL)));
            userModel.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_COL)));
            userModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COL)));
            userModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(PHONE_COL)));
            userModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS_COL)));
            cursor.close();
            return userModel;
        }
        cursor.close();
        return null; // Trả về null nếu không tìm thấy.
    }


    // Kiểm tra username và email đã tồn tại trong DB
    public boolean checkUsernameOrEmailExists(String username, String email){
        boolean exists = false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            // Kiểm tra nếu cả username và email đã tồn tại trong bảng
            String[] cols = {ID_COL, USERNAME_COL, EMAIL_COL};
            String condition = USERNAME_COL + " =? OR " + EMAIL_COL + " =? ";  // Kiểm tra username hoặc email
            String[] params = {username, email};

            Cursor cursor = db.query(TABLE_NAME, cols, condition, params, null, null, null);
            if (cursor.getCount() > 0) {
                exists = true;
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exists;
    }


    public int updatePassword(String newPassword, String username, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD_COL, newPassword);
        String condition = USERNAME_COL + " =? AND " + EMAIL_COL + " =? ";
        String[] params = {username, email};
        int update = db.update(TABLE_NAME, values, condition, params);
        db.close();
        return update;
    }
}
