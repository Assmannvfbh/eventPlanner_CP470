package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;

public class PartyListActivity extends AppCompatActivity {

    private ArrayList<Party> partylist;
    private RecyclerView recyclerView;
    protected EventService eventService;
    protected SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        recyclerView = findViewById(R.id.recycler_id);
        eventService = new EventService(this);
        db = eventService.getReadableDatabase();


        partylist = new ArrayList<>();

        setPartyInfo();
        setAdapter();
    }


    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(partylist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        
    }

    private void setPartyInfo() {

        EventLoader el = new EventLoader();
        el.execute();

//        partylist.add(new Party("Adian's 24th birthday"));
//        partylist.add(new Party("Amy's 27th birthday"));
//        partylist.add(new Party("Adriana's 26th birthday"));
//        partylist.add(new Party("Alex's 52th birthday"));
//        partylist.add(new Party("Ryan's 23th birthday"));
//        partylist.add(new Party("Max's 53th birthday"));
    }

    public class EventLoader extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            Cursor cursor = db.rawQuery("SELECT ID, TITLE FROM " + EventService.TABLE_NAME,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                partylist.add(new Party(cursor.getString(1), cursor.getInt(0)));
                cursor.moveToNext();
            }
            return null;
        }
    }
}