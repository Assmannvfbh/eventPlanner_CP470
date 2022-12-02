package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class MyEventsListActivity extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseService service;
    SQLiteDatabase db;
    List<Party> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_list);
        toolbar = this.findViewById(R.id.myEventsList_toolbar);
        setSupportActionBar(toolbar);

        eventList = new ArrayList<>();
        service = new DatabaseService(this);
        db = service.getWritableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu,menu);
        toolbar.setTitle(getResources().getString(R.string.myEventsList_toolbar_header));
        return super.onCreateOptionsMenu(menu);
    }


    public class EventLoader extends AsyncTask<String, Integer, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            Cursor cursor1 = db.rawQuery("SELECT * FROM " + DatabaseService.MEMBER_TABLE_NAME + " WHERE " + DatabaseService.USERNAME + " = ?",
                    new String[]{UserData.getUserData().getUsername()});
            cursor1.moveToFirst();
            while(!cursor1.isAfterLast()){
                String id = cursor1.getString(cursor1.getColumnIndexOrThrow(DatabaseService.EVENT_ID));
                Cursor cursor2 = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE " + DatabaseService.ID + " = ?",
                        new String[]{id});
                cursor2.moveToFirst();
                String eventName = cursor1.getString(cursor2.getColumnIndexOrThrow(DatabaseService.TITLE));
                eventList.add(new Party(eventName,Integer.parseInt(id)));
            }
            return null;
        }
    }

    public class MyEventsListAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }


}