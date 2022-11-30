package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;



import org.junit.Rule;

public class EventJoinServiceInstrumentaltest {


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
        EventJoinService eventJoinService = new EventJoinService(ctx);
        eventJoinService.joinEvent(eventID, username);
        SQLiteDatabase db = eventJoinService.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + EventJoinService.TABLE_NAME,null);
        Assert.assertTrue(cursor.getCount() > 0);
        db.close();
    }

    @Test
    public void leaveEvent() {
        EventJoinService eventJoinService = new EventJoinService(ctx);
        eventJoinService.leaveEvent(eventID, username);
        SQLiteDatabase db = eventJoinService.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + EventJoinService.TABLE_NAME,null);
        Assert.assertEquals(0,cursor.getCount());
        db.close();
    }


}
