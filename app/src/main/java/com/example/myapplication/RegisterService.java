package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegisterService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test1_Database";
    public static final String TABLE_NAME = "Test_Table";

    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";


    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + USERNAME + " text primary key, "
            + EMAIL + " text not null, "
            + PASSWORD + " text not null, "
            + NAME + " text not null, "
            + SURNAME + " text not null, "
            + DATE_OF_BIRTH + " text);";

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
