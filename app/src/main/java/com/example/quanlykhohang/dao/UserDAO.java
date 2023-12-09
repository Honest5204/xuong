package com.example.quanlykhohang.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class UserDAO {
    SharedPreferences sharedPreferences;
    private DbHelper base;

    public UserDAO(Context context) {
        base = new DbHelper(context);
        sharedPreferences = context.getSharedPreferences("INFOR", Context.MODE_PRIVATE);
    }

    @SuppressLint("Range")
    public User getUserById(String userId) {
        User user = null;
        SQLiteDatabase sqLiteDatabase = base.getReadableDatabase();
        String sql = "SELECT * FROM USER WHERE ID=?";

        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{userId})) {
            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getString(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("NAME")),
                        cursor.getString(cursor.getColumnIndex("HOMETOWN")),
                        cursor.getString(cursor.getColumnIndex("IMAGE")),
                        cursor.getString(cursor.getColumnIndex("POSITION")),
                        cursor.getString(cursor.getColumnIndex("LASTLOGIN")),
                        cursor.getString(cursor.getColumnIndex("LASTACTION"))
                );
            }
        }

        return user;
    }

    @SuppressLint("Range")
    public List<User> getAllListUser() {
        var list = new ArrayList<User>();
        var cursor = base.getReadableDatabase().rawQuery("SELECT * FROM USER", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new User(
                        cursor.getString(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("NAME")),
                        cursor.getString(cursor.getColumnIndex("HOMETOWN")),
                        cursor.getString(cursor.getColumnIndex("IMAGE")),
                        cursor.getString(cursor.getColumnIndex("POSITION")),
                        cursor.getString(cursor.getColumnIndex("LASTLOGIN")),
                        cursor.getString(cursor.getColumnIndex("LASTACTION"))
                ));
            }
            cursor.close();
        }
        return list;
    }

    public boolean checkUser(String username, String password) {
        var sqLiteDatabase = base.getReadableDatabase();
        var sql = "SELECT * FROM USER WHERE ID=? AND PASSWORD=?";
        try (var cursor = sqLiteDatabase.rawQuery(sql, new String[]{username, password})) {
            cursor.moveToFirst();
            var editor = sharedPreferences.edit();
            editor.putString("ID", cursor.getString(0));
            editor.putString("NAME", cursor.getString(1));
            editor.putString("POSITION", cursor.getString(3));
            editor.commit();
            return cursor.getCount() != 0;
        }
    }

    public int changePassword(String username, String oldPassword, String newPassword) {
        SQLiteDatabase sqLiteDatabase = base.getWritableDatabase();
        String sql = "SELECT * FROM USER WHERE ID=? AND PASSWORD=?";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{username, oldPassword})) {
            if (cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("PASSWORD", newPassword);

                String whereClause = "ID = ? AND PASSWORD = ?";
                String[] whereArgs = {username, oldPassword};

                int updatedRows = base.getWritableDatabase().update("USER", contentValues, whereClause, whereArgs);

                if (updatedRows > 0) {
                    // Đổi mật khẩu thành công
                    return 1;
                } else if (updatedRows == 0) {
                    // Không có dòng nào bị ảnh hưởng (không thay đổi mật khẩu)
                    return 0;
                } else {
                    // Sai tên đăng nhập hoặc mật khẩu cũ
                    return -1;
                }
            } else {
                // Sai tên đăng nhập hoặc mật khẩu cũ
                return -1;
            }
        }
    }

    public boolean createAccount(User user) {
        SQLiteDatabase sqLiteDatabase = base.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", user.getId());
        contentValues.put("NAME", user.getName());
        contentValues.put("PASSWORD", user.getPassWord());
        contentValues.put("POSITION", user.getPosition());
        contentValues.put("LASTLOGIN", user.getLastLogin());
        contentValues.put("CREATEDATE", user.getCreateDate());
        contentValues.put("LASTACTION", user.getLastAction());
        contentValues.put("HOMETOWN", user.getHomeTown());
        contentValues.put("IMAGE", user.getAvatar());
        long result = sqLiteDatabase.insert("USER", null, contentValues);
        return result > 0;
    }

    public boolean updateAccount( String username,String fullName, String position,String hometown,String image) {
        SQLiteDatabase sqLiteDatabase = base.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", fullName);
        contentValues.put("POSITION", position);
        contentValues.put("HOMETOWN", hometown);
        contentValues.put("IMAGE", image);
        var whereClause = "ID = ?";
        var whereArgs = new String[]{username};
        int updatedRows = sqLiteDatabase.update("USER", contentValues, whereClause, whereArgs);
        return updatedRows > 0;
    }

}
