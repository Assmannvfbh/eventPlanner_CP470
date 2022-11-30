package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EventService extends RegisterService {

    public static final String DATABASE_NAME = "Test1_Database";
    public static final String TABLE_NAME = "Event_Table";

    public static final String ID = "ID";
    public static final String ORGANIZER = "ORGANIZER";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LOCATION = "LOCATION";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String PRICE = "PRICE";
    public static final String EVENTTASKLIST = "TASKLIST";


    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_NAME + " ("
            + ID + " INTEGER primary key, "
            + ORGANIZER + " text not null, "
            + TITLE + " text not null, "
            + DESCRIPTION + " text not null, "
            + LOCATION + " text, "
            + DATE + " text not null, "
            + TIME + " text not null, "
            + PRICE + " text);";

    public EventService(Context ctx) {
        super(ctx);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
