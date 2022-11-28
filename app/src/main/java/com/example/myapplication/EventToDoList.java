package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventToDoList extends AppCompatActivity {


    EditText eventTaskList;

    SQLiteDatabase db;
    private RecyclerView tasksRecyclerView;
    //private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    //private List<ToDoAdapter.ToDoModel> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

      // eventTaskList = findViewById(R.id.eventTaskListText);

        EventService service = new EventService(this);
        db = service.getWritableDatabase();
        service.onCreate(db);


        // list of tasks
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // tasksAdapter = new ToDoAdapter(db,EventToDoList.this);
        //tasksRecyclerView.setAdapter(tasksAdapter);
    }



}