package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetails extends AppCompatActivity{
    DatabaseService databaseService;
    SQLiteDatabase db;
    TextView title;
    TextView organizer;
    TextView description;
    TextView date_time;
    TextView location;
    Toolbar toolbar;
    ImageView delete_button;
    Button joinButton;
    boolean admin;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseService = new DatabaseService(this);
        db = databaseService.getWritableDatabase();
        setContentView(R.layout.activity_event_details);
        title = this.findViewById(R.id.eventDetails_title);
        organizer = this.findViewById(R.id.eventDetails_organizer);
        description = this.findViewById(R.id.eventDetails_description);
        date_time = this.findViewById(R.id.eventDetail_date_time);
        location = this.findViewById(R.id.eventDetail_location);
        delete_button = this.findViewById(R.id.eventDetail_trash);
        joinButton = this.findViewById(R.id.eventDetails_join_button);
        delete_button.setVisibility(ImageView.INVISIBLE);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetailLoader loader = new EventDetailLoader(2);
                loader.execute();
            }
        });
        id = getIntent().getIntExtra("id", -1);
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
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE ID = ?", new String[]{strings[0]});
                cursor.moveToFirst();
                title.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.TITLE)));
                organizer.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.ORGANIZER)));
                description.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.DESCRIPTION)));
                date_time.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.DATE)));
                location.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.LOCATION)));
                cursor.close();
                return 0;
            }
            else if(mode == 1){
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE ADMIN = ?", new String[]{strings[0]});
                cursor.moveToFirst();
                if(cursor.getCount() > 0){
                    return 1;
                }
                cursor.close();
                return -1;
            }
            else if (mode == 2){
                db.delete(DatabaseService.EVENT_TABLE_NAME, DatabaseService.ID + " = ?", new String[]{String.valueOf(id)});
                return 2;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                delete_button.setVisibility(ImageView.VISIBLE);
                admin = true;
            }
            else if (integer == 2){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}