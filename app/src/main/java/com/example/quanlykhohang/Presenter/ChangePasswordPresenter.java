package com.example.quanlykhohang.Presenter;

import android.content.Context;

import com.example.quanlykhohang.Interface.ChangePasswordContract;
import com.example.quanlykhohang.dao.UserDAO;

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter{
    private ChangePasswordContract.View view;
    private UserDAO userDAO;

    public ChangePasswordPresenter(ChangePasswordContract.View view, Context context) {
        this.view = view;
        this.userDAO = new UserDAO(context);
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            view.showChangePasswordError("Please enter old and new passwords");
        } else {
            int result = userDAO.changePassword(userId, oldPassword, newPassword);
            if (result == 1) {
                view.showChangePasswordSuccess();
            } else if (result == 0) {
                view.showChangePasswordError("Old password is incorrect");
            } else {
                view.showChangePasswordError("Failed to change password");
            }
        }
    }
}
