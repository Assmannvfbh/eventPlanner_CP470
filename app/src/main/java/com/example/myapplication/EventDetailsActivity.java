package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ContentValues;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class EventDetailsActivity extends AppCompatActivity{
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
    Dialog dialog;
    Dialog helpDialog;

    boolean isAdmin;
    boolean isMember;
    int id;

    public final int POPULATE_EVENT_ACTION = 0;
    public final int DELETE_EVENT_ACTION = 2;
    public final int JOIN_EVENT_ACTION = 3;
    public final int CHECK_MEMBER_ACTION = 4;
    public final int CHECK_MEMBER_ACTION_TRUE = 5;
    public final int CHECK_MEMBER_ACTION_FALSE = 6;
    public final int LEAVE_EVENT_ACTION = 7;


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
        dialog = new Dialog(this);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMember) {
                    EventDetailLoader eventDetailLoader = new EventDetailLoader(JOIN_EVENT_ACTION);
                    eventDetailLoader.execute();
                    checkMember();
                }
                else if(isMember){
                    EventDetailLoader eventDetailLoader = new EventDetailLoader(LEAVE_EVENT_ACTION);
                    eventDetailLoader.execute();
                    checkMember();
                }
            }
        });
        delete_button.setVisibility(ImageView.INVISIBLE);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        id = getIntent().getIntExtra("id", -1);
        EventDetailLoader loader = new EventDetailLoader(POPULATE_EVENT_ACTION);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menu_standard_help:
                openHelpDialog();
                break;

            case R.id.menu_standard_logout:
                UserData.getUserData().clear();
                Intent intent = new Intent(EventDetailsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        return true;
    }


    public void checkAdmin(){
        EventDetailLoader loader = new EventDetailLoader(1);
        loader.execute(UserData.getUserData().getUsername());
    }

    public void checkMember(){
        EventDetailLoader loader = new EventDetailLoader(CHECK_MEMBER_ACTION);
        loader.execute();
    }

    public void openDialog(){
        dialog.setContentView(R.layout.dialog_delete);
        Button okButton = dialog.findViewById(R.id.delete_dialog_ok);
        Button cancelButton = dialog.findViewById(R.id.delete_dialog_cancel);
        TextView text = dialog.findViewById(R.id.delete_dialog_text);

        text.setText(getResources().getString(R.string.delete_dialog_text));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetailLoader loader = new EventDetailLoader(DELETE_EVENT_ACTION);
                loader.execute();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openHelpDialog() {
        helpDialog = new Dialog(this);
        helpDialog.setContentView(R.layout.dialog_help);
        Button okButton = helpDialog.findViewById(R.id.help_dialog_ok);
        TextView text = helpDialog.findViewById(R.id.help_dialog_text);

        text.setText(getResources().getString(R.string.help_dialog_event_details));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.show();
    }




    public class EventDetailLoader extends AsyncTask<String, Integer, Integer>{

        int mode;

        public EventDetailLoader(int mode){
            this.mode = mode;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            if(mode == POPULATE_EVENT_ACTION) {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE ID = ?", new String[]{strings[0]});
                cursor.moveToFirst();
                title.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.TITLE)));
                organizer.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.ORGANIZER)));
                description.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.DESCRIPTION)));
                date_time.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.DATE)));
                location.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseService.LOCATION)));
                cursor.close();
                return POPULATE_EVENT_ACTION;
            }
            else if(mode == 1){
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.EVENT_TABLE_NAME + " WHERE ADMIN = ? AND ID = ?", new String[]{strings[0],String.valueOf(id)});
                cursor.moveToFirst();
                if(cursor.getCount() > 0){
                    return 1;
                }
                cursor.close();
                return -1;
            }
            else if (mode == DELETE_EVENT_ACTION){
                db.delete(DatabaseService.EVENT_TABLE_NAME, DatabaseService.ID + " = ?", new String[]{String.valueOf(id)});
                db.delete(DatabaseService.MEMBER_TABLE_NAME, DatabaseService.EVENT_ID + " = ?", new String[]{String.valueOf(id)});
                return DELETE_EVENT_ACTION;
            }
            else if(mode == JOIN_EVENT_ACTION){
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseService.EVENT_ID,id);
                contentValues.put(DatabaseService.USERNAME,UserData.getUserData().getUsername());

                db.insert(DatabaseService.MEMBER_TABLE_NAME,null,contentValues);
                return JOIN_EVENT_ACTION;
            }
            else if(mode == CHECK_MEMBER_ACTION){
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseService.MEMBER_TABLE_NAME + " WHERE " + DatabaseService.USERNAME + " = ? AND " + DatabaseService.EVENT_ID + " = ?",
                        new String[]{UserData.getUserData().getUsername(), String.valueOf(id)});
                cursor.moveToFirst();
                if(cursor.getCount() > 0){
                    return CHECK_MEMBER_ACTION_TRUE;
                }
                cursor.close();
                return CHECK_MEMBER_ACTION_FALSE;
            }
            else if(mode == LEAVE_EVENT_ACTION){
                db.delete(DatabaseService.MEMBER_TABLE_NAME, DatabaseService.EVENT_ID + " = ? AND " + DatabaseService.USERNAME + " = ?",
                        new String[]{String.valueOf(id), UserData.getUserData().getUsername()});
                return LEAVE_EVENT_ACTION;
            }

            return -100;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1){
                delete_button.setVisibility(ImageView.VISIBLE);
                joinButton.setVisibility(View.INVISIBLE);

                isAdmin = true;
            }
            else if (integer == 2){
                finish();
            }
            else if(integer == CHECK_MEMBER_ACTION_TRUE){
                joinButton.setText(getResources().getString(R.string.eventDetail_leave_button));
                isMember = true;
            }
            else if(integer == CHECK_MEMBER_ACTION_FALSE){
                joinButton.setText(getResources().getString(R.string.eventDetail_join_button));
                isMember = false;
            }
            else if(integer == LEAVE_EVENT_ACTION){
                Snackbar.make(joinButton, "You left the event", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isAdmin = false;
        checkAdmin();
        checkMember();
    }
}