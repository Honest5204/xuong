package com.example.quanlykhohang.Interface;

public interface ChangePasswordContract {
    interface View {
        void showChangePasswordSuccess();

        void showChangePasswordError(String message);
    }

    interface Presenter {
        void changePassword(String userId, String oldPassword, String newPassword);
    }
}
