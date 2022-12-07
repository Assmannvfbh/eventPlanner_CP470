package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventToDoList extends AppCompatActivity {
    TextView eventTitle;
    TextView eventOrganizer;

    SQLiteDatabase db;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    String [] tasks = new String [] {"Find event venue",
    "Send invitations", "Buy supplies", "Grocery Shopping",
            "Get the food", " Set up", " Take down and clean up"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        EventService service = new EventService(this);
        db = service.getWritableDatabase();
        service.onCreate(db);


        eventTitle = findViewById(R.id.eventTitleText);
        eventOrganizer = findViewById(R.id.eventOrganizerText);
        lvItems = (ListView) findViewById(R.id.lvItems);
            items = new ArrayList<String>();
            itemsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            lvItems.setAdapter(itemsAdapter);
            items.add("Find Venue");
            items.add("Send Invitations");
            items.add("Buy Supplies");
            items.add("Set Up Event");

    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        setupListViewListener();

    }
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
    }
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EventToDoList(View view) {
        Map<String, String> map = new HashMap<>();
        map.get(eventTitle.getText().toString());
        map.get(eventOrganizer.getText().toString());

        EventToDoList.ToDoQuery eventQuery = new EventToDoList.ToDoQuery();
        eventQuery.execute(map);

    }
    public class ToDoQuery extends AsyncTask<Map<String, String>, Integer, Integer> {

        @Override
        protected Integer doInBackground(Map<String, String>... maps) {
            Map<String, String> entries = maps[0];
            ContentValues contentValues = new ContentValues();
            contentValues.get(entries.get("title"));
            contentValues.get(entries.get("organizer"));

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