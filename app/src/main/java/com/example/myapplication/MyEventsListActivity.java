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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyEventsListActivity extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseService service;
    SQLiteDatabase db;
    List<Event> eventList;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_list);
        toolbar = this.findViewById(R.id.myEventsList_toolbar);
        setSupportActionBar(toolbar);

        list = this.findViewById(R.id.myEventsList_recycler_view);
        eventList = new ArrayList<>();
        service = new DatabaseService(this);
        db = service.getWritableDatabase();

    }

    @Override
    protected void onStart() {
        eventList.clear();
        super.onStart();
        EventLoader loader2 = new EventLoader(1);
        loader2.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu,menu);
        toolbar.setTitle(getResources().getString(R.string.myEventsList_toolbar_header));
        return super.onCreateOptionsMenu(menu);
    }

    private void setAdapter() {
        MyEventsListAdapter adapter = new MyEventsListAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);

    }


    public class EventLoader extends AsyncTask<String, Integer, Integer>{

        int mode;

        public EventLoader(int mode) {
            this.mode = mode;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            if(mode == 1){
                Cursor cursor1 = db.rawQuery("SELECT ID FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE " + DatabaseService.ADMIN + " = ?",
                        new String[]{UserData.getUserData().getUsername()});
                cursor1.moveToFirst();
                List<Integer> idList = new ArrayList<>();
                while (!cursor1.isAfterLast()){
                    int i = cursor1.getInt(0);
                    idList.add(i);
                    cursor1.moveToNext();
                }
                Cursor cursor2;
                for(Integer id : idList){
                    cursor2 = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE " + DatabaseService.ID + " = ?",
                            new String[]{String.valueOf(id)});
                    cursor2.moveToFirst();
                    String eventName = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.TITLE));
                    String organizer = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.ORGANIZER));
                    String admin = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.ADMIN));
                    eventList.add(new Event(eventName, id, organizer,admin));
                    cursor2.close();
                }
                return 1;
            }
            else if (mode == 2) {

                Cursor cursor1 = db.rawQuery("SELECT * FROM " + DatabaseService.MEMBER_TABLE_NAME + " WHERE " + DatabaseService.USERNAME + " = ?",
                        new String[]{UserData.getUserData().getUsername()});
                cursor1.moveToFirst();
                Cursor cursor2;
                while (!cursor1.isAfterLast()) {
                    String id = cursor1.getString(cursor1.getColumnIndexOrThrow(DatabaseService.EVENT_ID));
                    cursor2 = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE " + DatabaseService.ID + " = ?",
                            new String[]{id});
                    cursor2.moveToFirst();
                    String eventName = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.TITLE));
                    String organizer = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.ORGANIZER));
                    String admin = cursor2.getString(cursor2.getColumnIndexOrThrow(DatabaseService.ADMIN));
                    eventList.add(new Event(eventName, Integer.parseInt(id), organizer, admin));
                    cursor2.close();
                    cursor1.moveToNext();
                }
                cursor1.close();

                return 2;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                EventLoader loader = new EventLoader(2);
                loader.execute();
            }
            else if(integer == 2){
                setAdapter();
            }
        }
    }

    public class MyEventsListAdapter extends RecyclerView.Adapter {

        TextView title;
        TextView subtitle;
        ImageView admin_image;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_row,parent,false);
            return new MyEventListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String name = eventList.get(position).getParty_name();
            String organizer = eventList.get(position).getOrganizer();
            String admin = eventList.get(position).getAdmin();
            int id = eventList.get(position).getID();

            title = holder.itemView.findViewById(R.id.event_row_title);
            subtitle = holder.itemView.findViewById(R.id.event_row_subtitle);
            admin_image = holder.itemView.findViewById(R.id.party_row_admin);

            title.setText(name);
            subtitle.setText(organizer);
            if(admin.equals(UserData.getUserData().getUsername())){
                admin_image.setVisibility(View.VISIBLE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EventDetails.class);
                    intent.putExtra("id", id);
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }
    }

    public class MyEventListViewHolder extends RecyclerView.ViewHolder{

        public MyEventListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


}