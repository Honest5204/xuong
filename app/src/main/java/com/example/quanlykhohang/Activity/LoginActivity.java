package com.example.quanlykhohang.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlykhohang.Interface.LoginContract;
import com.example.quanlykhohang.Presenter.LoginPresenter;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.database.DbHelper;
import com.example.quanlykhohang.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private ActivityLoginBinding binding;
    private LoginContract.Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginPresenter = new LoginPresenter(this, this);
        var sharedPreferences = getSharedPreferences("INFOR", MODE_PRIVATE);
        var rememberPassword = sharedPreferences.getBoolean("SAVE_PASSWORD", false);
        binding.checkBox.setChecked(rememberPassword);
        if (rememberPassword) {
            // Nếu Checkbox đã được chọn, thì khôi phục tài khoản và mật khẩu
            var savedUsername = sharedPreferences.getString("ID", "");
            var savedPassword = sharedPreferences.getString("PASSWORD", "");
            binding.edtUsername.setText(savedUsername);
            binding.edtPassword.setText(savedPassword);
        }

        binding.btnDangNhap.setOnClickListener(view -> {
            String username = binding.edtUsername.getText().toString();
            String password = binding.edtPassword.getText().toString();
            boolean rememberPass = binding.checkBox.isChecked();

            loginPresenter.loginUser(username, password, rememberPass);
        });
    }
    @Override
    public void showLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError(String message) {
        binding.edtUsername.setError(message);
        binding.edtPassword.setError(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}