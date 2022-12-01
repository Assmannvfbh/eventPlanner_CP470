package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test1_Database";
    public static final String TEST_TABLE_NAME = "Test_Table";
    public static final String MEMBER_TABLE_NAME = "Member_Table";
    public static final String EVENT_TABLE_NAME = "Event_Table";

    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String DATE_OF_BIRTH = "DATE_OF_BIRTH";
    public static final String EVENT_ID = "EVENT_ID";

    public static final String ID = "ID";
    public static final String ORGANIZER = "ORGANIZER";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LOCATION = "LOCATION";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String PRICE = "PRICE";
    public static final String ADMIN = "ADMIN";


    private static final String REGISTRATION_CREATE = "create table "
            + TEST_TABLE_NAME + "("
            + USERNAME + " text primary key, "
            + EMAIL + " text not null, "
            + PASSWORD + " text not null, "
            + NAME + " text not null, "
            + SURNAME + " text not null, "
            + DATE_OF_BIRTH + " text);";

    private static final String MEMBER_TABLE_CREATE = "create table "
            + MEMBER_TABLE_NAME + " ("
            + USERNAME + " text not null, "
            + EVENT_ID + " integer not null, "
            + "PRIMARY KEY(USERNAME, EVENT_ID));";

    private static final String EVENT_TABLE_CREATE = "create table "
            + EVENT_TABLE_NAME + " ("
            + ID + " INTEGER primary key, "
            + ORGANIZER + " text not null, "
            + TITLE + " text not null, "
            + DESCRIPTION + " text not null, "
            + LOCATION + " text, "
            + DATE + " text not null, "
            + TIME + " text not null, "
            + PRICE + " text, "
            + ADMIN + " text not null);";

    public DatabaseService(Context ctx){
        super(ctx, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REGISTRATION_CREATE);
        db.execSQL(MEMBER_TABLE_CREATE);
        db.execSQL(EVENT_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
