package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates a table to map the usernames with eventID's. Additionally it provides two methods
 * to insert and delete data to this table. This class implements the joi/leave course functionality
 * of the app
 */
public class EventJoinService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test1_Database";
    public static final String TABLE_NAME = "Member_Table";

    public static final String EVENT_ID = "EVENT_ID";
    public static final String USERNAME = "USERNAME";

    public static final int LEAVE_EVENT = 0;
    public static final int JOIN_EVENT = 1;

    public SQLiteDatabase db;



    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + USERNAME + " text not null, "
            + EVENT_ID + " integer not null, "
            + "PRIMARY KEY(USERNAME, EVENT_ID));";

    public EventJoinService(Context ctx){
        super(ctx, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean joinEvent(int eventID, String username){
        db = super.getWritableDatabase();
        EventInteraction eventInteraction = new EventInteraction(JOIN_EVENT);
        eventInteraction.execute(String.valueOf(eventID), username);
        return false;
    }

    public boolean leaveEvent(int eventID, String username){
        db = super.getWritableDatabase();
        EventInteraction eventInteraction = new EventInteraction(LEAVE_EVENT);
        eventInteraction.execute(String.valueOf(eventID), username);
        return false;
    }

    public class EventInteraction extends AsyncTask<String, Integer, Integer>{

        public final int JOINED_EVENT = 1;
        public final int LEFT_EVENT = 0;
        int action;


        public EventInteraction(int action) {
            this.action = action;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            if(action == JOIN_EVENT){
                ContentValues values = new ContentValues();
                String eventId = strings[0];
                String username = strings[1];
                values.put(USERNAME,username);
                values.put(EVENT_ID, eventId);

                db.insert(TABLE_NAME,null, values);
                return JOINED_EVENT;
            }
            else if(action == LEAVE_EVENT){
                String[] array = new String[2];
                array[0] = strings[1]; //username
                array[1] = String.valueOf(strings[0]); //eventID
                db.delete(TABLE_NAME, USERNAME + " = ? AND " + EVENT_ID + " = ?", array );
                return LEFT_EVENT;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            db.close();
        }
    }



}
