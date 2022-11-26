package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {
    EditText eventTitle;
    EditText eventOrganizer;
    EditText eventDescription;
    EditText eventDate;
    EditText eventTime;
    EditText eventPrice;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventTitle = (EditText) findViewById(R.id.eventTitleText);
        eventOrganizer = (EditText) findViewById(R.id.eventOrganizerText);
        eventDescription = (EditText) findViewById(R.id.eventDescriptionText);
        eventDate = (EditText) findViewById(R.id.editTextDate);
        eventTime = (EditText) findViewById(R.id.editTextTime);
        eventPrice = (EditText) findViewById(R.id.priceText);

        EventService service = new EventService(this);
        db = service.getWritableDatabase();
        service.onCreate(db);

    }

    public void createEvent(View view){
        Map<String, String> map = new HashMap<>();
        map.put("title", eventTitle.getText().toString());
        map.put("organizer_id", eventOrganizer.getText().toString());
        map.put("description", eventDescription.getText().toString());
        map.put("time", eventTime.getText().toString());
        map.put("date", eventDate.getText().toString());
        map.put("price", eventPrice.getText().toString());

        EventQuery eventQuery = new EventQuery();
        eventQuery.execute(map);


    }

    public class EventQuery extends AsyncTask<Map<String, String>, Integer, Integer> {

        @Override
        protected Integer doInBackground(Map<String, String>... maps) {
            Map<String, String> entries = maps[0];
            ContentValues contentValues = new ContentValues();
            contentValues.put(EventService.TITLE, entries.get("title"));
            contentValues.put(EventService.ORGANIZER, entries.get("organizer"));
            contentValues.put(EventService.DESCRIPTION, entries.get("description"));
            contentValues.put(EventService.LOCATION, entries.get("location"));
            contentValues.put(EventService.PRICE, entries.get("price"));
            contentValues.put(EventService.DATE, entries.get("date"));
            contentValues.put(EventService.TIME, entries.get("time"));

            try {
                db.insert(EventService.TABLE_NAME, null, contentValues);
                return 1;
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer){
            super.onPostExecute(integer);
            if(integer==1){
                finish();
            }
        }
    }
}