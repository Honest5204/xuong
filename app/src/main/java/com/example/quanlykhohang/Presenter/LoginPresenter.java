package com.example.quanlykhohang.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.quanlykhohang.Interface.LoginContract;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.database.DbHelper;

public class LoginPresenter implements LoginContract.Presenter{
    private LoginContract.View view;
    private UserDAO userDAO;
    private SharedPreferences sharedPreferences;
    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.userDAO = new UserDAO(context);
        this.sharedPreferences = context.getSharedPreferences("INFOR", Context.MODE_PRIVATE);
    }
    @Override
    public void loginUser(String username, String password, boolean rememberPassword) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showLoginError("Please enter complete login information");
        } else {
            try {
                if (userDAO.checkUser(username, password)) {
                    view.showLoginSuccess();
                    if (rememberPassword) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ID", username);
                        editor.putString("PASSWORD", password);
                        editor.putBoolean("SAVE_PASSWORD", true);
                        editor.apply();
                    } else {
                        // Nếu Checkbox không được chọn, xóa tài khoản và mật khẩu đã lưu
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("SAVE_PASSWORD");
                        editor.remove("PASSWORD");
                        editor.putBoolean("SAVE_PASSWORD", false);
                        editor.apply();
                    }
                    var id = sharedPreferences.getString("ID", "");
                    var base = new DbHelper((Context) view);
                    base.updateLasLogin(id);
                    Toast.makeText((Context) view, "Logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    view.showLoginError("Username or password is incorrect");
                }
            } catch (Exception e) {
                Log.e("Login error", "ERROR: " + e.getMessage());
                view.showLoginError("ERROR");
            }
        }
    }
}
