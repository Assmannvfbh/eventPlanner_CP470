package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetSocketAddress;

public class EventDetails extends AppCompatActivity{
    EventService eventService;
    SQLiteDatabase db;
    TextView title;
    TextView organizer;
    TextView description;
    TextView date_time;
    TextView location;
    Toolbar toolbar;
    ImageView delete_button;
    boolean admin;
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
        delete_button = this.findViewById(R.id.eventDetail_trash);
        delete_button.setVisibility(ImageView.INVISIBLE);
        eventService = new EventService(this);
        id = getIntent().getIntExtra("id", -1);
        db = eventService.getWritableDatabase();
        checkAdmin();
        EventDetailLoader loader = new EventDetailLoader(0);
        loader.execute(String.valueOf(id));
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

    public void checkAdmin(){
        EventDetailLoader loader = new EventDetailLoader(1);
        loader.execute(UserData.getUserData().getUsername());
    }



    public class EventDetailLoader extends AsyncTask<String, Integer, Integer>{


        int mode;

        public EventDetailLoader(int mode){
            this.mode = mode;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            if(mode == 0) {
                Cursor cursor = db.rawQuery("SELECT * FROM " + EventService.TABLE_NAME + " WHERE ID = ?", new String[]{strings[0]});
                cursor.moveToFirst();
                title.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.TITLE)));
                organizer.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.ORGANIZER)));
                description.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.DESCRIPTION)));
                date_time.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.DATE)));
                location.setText(cursor.getString(cursor.getColumnIndexOrThrow(EventService.LOCATION)));
                cursor.close();
                return 0;
            }
            else if(mode == 1){
                Cursor cursor = db.rawQuery("SELECT * FROM " + EventService.TABLE_NAME + " WHERE ADMIN = ?", new String[]{strings[0]});
                cursor.moveToFirst();
                if(cursor.getCount() > 0){
                    return 1;
                }
                cursor.close();
                return -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                delete_button.setVisibility(ImageView.VISIBLE);
                admin = true;
                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.delete(EventService.TABLE_NAME, EventService.ID + " = ?", new String[]{String.valueOf(id)});
                        Intent intent = new Intent(v.getContext(),PartyListActivity.class);
                        intent.putExtra("update", getResources().getString(R.string.snackbar_event_deleted));
                        v.getContext().startActivity(intent);

                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}