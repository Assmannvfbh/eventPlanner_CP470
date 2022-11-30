package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventToDoList extends AppCompatActivity {


    EditText eventTaskList;
    SQLiteDatabase db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    ArrayList<ToDoModel> taskList;
    String [] tasks = new String [] {"Find event venue",
    "Send invitations", "Buy supplies", "Grocery Shopping",
            "Get the food", " Set up", " Take down and clean up"};
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
        tasksAdapter = new ToDoAdapter(this, getTasks());
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tasksRecyclerView.setAdapter(tasksAdapter);
    }

    private ArrayList <ToDoModel> getTasks() {
        for (int i =0; i<tasks.length; i++ ) {
            taskList.add( new ToDoModel(tasks [i], false));
        }
        return taskList;
    }


}