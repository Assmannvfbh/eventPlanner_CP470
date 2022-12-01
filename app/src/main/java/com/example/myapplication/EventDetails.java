package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity{
    EventService eventService;
    SQLiteDatabase db;
    TextView title;
    TextView organizer;
    TextView description;
    TextView date_time;
    TextView location;
    Toolbar toolbar;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        title = this.findViewById(R.id.eventDetails_title);
        organizer = this.findViewById(R.id.eventDetails_organizer);
        description = this.findViewById(R.id.eventDetails_description);
        date_time = this.findViewById(R.id.eventDetail_date_time);
        location = this.findViewById(R.id.eventDetail_location);
        eventService = new EventService(this);
        id = getIntent().getIntExtra("id", -1);
        db = eventService.getWritableDatabase();
        EventDetailLoader loader = new EventDetailLoader();
        loader.execute(id);
        toolbar = findViewById(R.id.eventDetail_toolbar);
        toolbar.setTitle(R.string.eventDetail_toolbar_header);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    public class EventDetailLoader extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + EventService.TABLE_NAME + " WHERE ID = ?", new String[]{String.valueOf(integers[0])});
            cursor.moveToFirst();
            title.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.TITLE)));
            organizer.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.ORGANIZER)));
            description.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.DESCRIPTION)));
            date_time.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.DATE)));
            location.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.LOCATION)));
            return null;
        }
    }

}