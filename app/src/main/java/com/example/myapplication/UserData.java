package com.example.myapplication;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//designed as singleton
public class UserData {

    private static UserData userData;

    private String username;
    private String email;
    private String dateOfBirth;
    private String name;
    private String surname;


    private UserData(){}

    public static UserData getUserData(){
        if(userData == null){
            userData = new UserData();
        }
        return userData;
    }



    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
