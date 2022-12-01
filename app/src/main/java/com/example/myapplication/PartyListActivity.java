package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PartyListActivity extends AppCompatActivity {

    private ArrayList<Party> partylist;
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
        toolbar.setTitle(getResources().getString(R.string.eventList_toolbar_header));

        recyclerView = findViewById(R.id.recycler_id);
        databaseService = new DatabaseService(this);
        db = databaseService.getReadableDatabase();

//        if(getIntent().hasExtra("update")){
//            Snackbar.make(recyclerView,getIntent().getStringExtra("update"),Snackbar.LENGTH_SHORT).show();
//        }


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
        inflater.inflate(R.menu.standard_menu,menu);
        return super.onCreateOptionsMenu(menu);
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
            Cursor cursor = db.rawQuery("SELECT ID, TITLE FROM " + DatabaseService.EVENT_TABLE_NAME,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                partylist.add(new Party(cursor.getString(1), cursor.getInt(0)));
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