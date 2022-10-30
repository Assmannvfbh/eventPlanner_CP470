package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegisterService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test_Database";
    public static final String TABLE_NAME = "Test_Table";

    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + USERNAME
            + " String primary key autoincrement, " + EMAIL
            + " text not null, " + PASSWORD + " text not null);";

    public RegisterService(Context ctx){
        super(ctx, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
