package com.example.quanlykhohang.Interface;

public interface LoginContract {
    interface View {
        void showLoginSuccess();

        void showLoginError(String message);
    }

    interface Presenter {
        void loginUser(String username, String password, boolean rememberPassword);
    }
}
