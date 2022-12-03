package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PartyListActivity extends AppCompatActivity {

    private ArrayList<Event> partylist;
    private RecyclerView recyclerView;
    protected DatabaseService databaseService;
    protected SQLiteDatabase db;
    View view;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        toolbar = this.findViewById(R.id.eventList_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_id);
        databaseService = new DatabaseService(this);
        db = databaseService.getReadableDatabase();

        partylist = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        partylist.clear();
        super.onStart();
        EventLoader el = new EventLoader();
        el.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_details_menu,menu);
        toolbar.setTitle(getResources().getString(R.string.eventList_toolbar_header));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.event_details_toolbar_about:
                Snackbar.make(this.toolbar, getResources().getString(R.string.login_about_Niklas), Toast.LENGTH_SHORT).show();
                break;
            case R.id.event_details_toolbar_plus:
                Intent intent = new Intent(PartyListActivity.this, CreateEvent.class);
                startActivity(intent);
        }
        return true;
    }


    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(partylist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        
    }



    public class EventLoader extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                partylist.add(new Event(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.TITLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseService.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.ORGANIZER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.ADMIN))));
                cursor.moveToNext();
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            setAdapter();
        }
    }
}