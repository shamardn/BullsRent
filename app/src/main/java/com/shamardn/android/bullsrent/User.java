package com.shamardn.android.bullsrent;

import java.io.Serializable;

public class User implements Serializable {
    String fullName;
    String userName;
    String email;
    String phone;
    String password;

    public User() {
    }

    public User(String fullName, String userName, String email, String phone, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
