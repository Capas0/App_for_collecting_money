package ru.hse.edu.grudina.obshak;

import android.net.Uri;

import java.util.Date;

public class User {
    private String id;
    private String nickName;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private Uri photo;
    private Date birthday;

    public User(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {

    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {

    }
}
