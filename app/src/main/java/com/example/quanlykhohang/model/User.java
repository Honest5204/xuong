package com.example.quanlykhohang.model;

public class User {
    String id; //id người dùng
    String name; //tên người dùng
    String passWord;
    String homeTown;
    String avatar;
    String position;//chức vụ
    String lastLogin;//lần đăng nhập cuối
    String createDate;//ngày tạo
    String lastAction;//lần thao tác cuối

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User(String name, String homeTown, String avatar, String position) {
        this.name = name;
        this.homeTown = homeTown;
        this.avatar = avatar;
        this.position = position;
    }

    public User(String id,String name,String passWord,String position,String lastLogin,String currentDate,String currentDatee,String hometown,String imgavatar) {
        this.id = id;
        this.name = name;
        this.passWord = passWord;
        this.homeTown = hometown;
        this.avatar = imgavatar;
        this.position = position;
        this.lastLogin = lastLogin;
        this.createDate = currentDate;
        this.lastAction = currentDatee;
    }

    public User() {
    }

    public User(String id, String name, String homeTown, String avatar, String position, String lastLogin, String lastAction) {
        this.id = id;
        this.name = name;
        this.homeTown = homeTown;
        this.avatar = avatar;
        this.position = position;
        this.lastLogin = lastLogin;
        this.lastAction = lastAction;
    }
}
