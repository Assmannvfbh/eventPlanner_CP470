package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventMemberServiceInstrumentaltest {


    public Context ctx;
    public int eventID = 12345;
    public String username = "Assmannvfbh";


    @Before
    public void createDB(){
        ctx = ApplicationProvider.getApplicationContext();

    }
    @Test
    public void onCreate() {

    }

    @Test
    public void joinEvent() {
        EventMemberService eventJoinService = new EventMemberService(ctx);
        eventJoinService.joinEvent(eventID, username);
        SQLiteDatabase db = eventJoinService.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + EventMemberService.TABLE_NAME,null);
        Assert.assertTrue(cursor.getCount() > 0);
        db.close();
    }

    @Test
    public void leaveEvent() {
        EventMemberService eventJoinService = new EventMemberService(ctx);
        eventJoinService.leaveEvent(eventID, username);
        SQLiteDatabase db = eventJoinService.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + EventMemberService.TABLE_NAME,null);
        Assert.assertEquals(0,cursor.getCount());
        db.close();
    }


}
