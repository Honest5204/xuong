package com.example.quanlykhohang.Interface;

import com.example.quanlykhohang.model.User;

import java.util.List;

public interface UserContract {
    interface View {
        void showUsersList(List<User> userList);
        void showUserDetails(User user);
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void getUserDetails(String userId);
        void loadUsers();
        void addUser(User user);
        void updateUser(String userId, String name, String position, String homeTown, String image);
    }
}
