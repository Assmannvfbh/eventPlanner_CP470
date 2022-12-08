package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PartyListActivity extends AppCompatActivity {

    private ArrayList<Event> partylist;
    private RecyclerView recyclerView;
    protected DatabaseService databaseService;
    protected SQLiteDatabase db;
    View view;
    Dialog helpDialog;

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
        Intent intent;

        switch(id){
            case R.id.event_details_toolbar_help:
                openHelpDialog();
                break;
            case R.id.event_details_toolbar_plus:
                intent = new Intent(PartyListActivity.this, CreateEventActivity.class);
                startActivity(intent);
                break;
            case R.id.event_details_toolbar_logout:
                UserData.getUserData().clear();
                intent = new Intent(PartyListActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void openHelpDialog() {
        helpDialog = new Dialog(this);
        helpDialog.setContentView(R.layout.dialog_help);
        Button okButton = helpDialog.findViewById(R.id.help_dialog_ok);
        TextView text = helpDialog.findViewById(R.id.help_dialog_text);

        text.setText(getResources().getString(R.string.help_dialog_party_list));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.show();
    }


    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(partylist);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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