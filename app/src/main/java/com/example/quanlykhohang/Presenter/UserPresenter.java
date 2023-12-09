package com.example.quanlykhohang.Presenter;

import android.content.Context;

import com.example.quanlykhohang.Interface.UserContract;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.model.User;

import java.util.List;

public class UserPresenter implements UserContract.Presenter{
    private UserContract.View view;
    private UserDAO userDAO;

    public UserPresenter(UserContract.View view, Context context) {
        this.view = view;
        this.userDAO = new UserDAO(context);
    }


    @Override
    public void getUserDetails(String userId) {
        try {
            User user = userDAO.getUserById(userId);

            if (user != null) {
                view.showUserDetails(user);
            } else {
                view.showErrorMessage("Không tìm thấy người dùng");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi lấy thông tin người dùng");
        }
    }

    @Override
    public void loadUsers() {
        List<User> userList = userDAO.getAllListUser();
        if (userList != null && userList.size() > 0) {
            view.showUsersList(userList);
        } else {
            view.showErrorMessage("Không có người dùng.");
        }
    }

    @Override
    public void addUser(User user) {
        try {
            // Gọi hàm thêm người dùng từ DAO
            boolean success = userDAO.createAccount(user);

            // Hiển thị thông báo kết quả đến View
            if (success) {
                view.showSuccessMessage("Thêm người dùng thành công");
                // Reload danh sách người dùng sau khi thêm
                loadUsers();
            } else {
                view.showErrorMessage("Thêm người dùng thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Đã xảy ra lỗi khi thêm người dùng");
        }
    }

    @Override
    public void updateUser(String userId, String name, String position, String homeTown, String image) {
        if (name.isEmpty() || position.isEmpty() || homeTown.isEmpty()) {
            view.showErrorMessage("Fields cannot be empty");
        } else if (image.isEmpty()) {
            view.showErrorMessage("Image cannot be empty");
        } else {
            try {
                boolean success = userDAO.updateAccount(userId, name, position, homeTown, image);

                if (success) {
                    view.showSuccessMessage("Cập nhật người dùng thành công");
                    // Reload danh sách người dùng sau khi cập nhật
                    loadUsers();
                } else {
                    view.showErrorMessage("Cập nhật người dùng thất bại");
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.showErrorMessage("Đã xảy ra lỗi khi cập nhật người dùng");
            }
        }
    }
}
