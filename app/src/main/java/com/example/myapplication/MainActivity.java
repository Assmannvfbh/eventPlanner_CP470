package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView profile;
    ImageView myEvents;

    ImageView party_list;

    ImageView map;
    Button eventList;

    Dialog helpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        profile = (ImageView) findViewById(R.id.profileIcon);
        myEvents = (ImageView) findViewById(R.id.main_my_events_icon);

        party_list = (ImageView) findViewById(R.id.dancingIcon);

        map = (ImageView) findViewById(R.id.main_map_icon);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(MainActivity.this, MapView.class);
                startActivity(mapIntent);
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(MainActivity.this, MyEventsListActivity.class);
                startActivity(createEvent);
            }
        });

        //here

        party_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPartyList = new Intent(MainActivity.this, PartyListActivity.class);
                startActivity(createPartyList);
            }
        });


    }

    public void openHelpDialog() {
        helpDialog = new Dialog(this);
        helpDialog.setContentView(R.layout.dialog_help);
        Button okButton = helpDialog.findViewById(R.id.help_dialog_ok);
        TextView text = helpDialog.findViewById(R.id.help_dialog_text);

        text.setText(getResources().getString(R.string.help_dialog_main));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.show();
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        return true;
    }



    public void setupLayout(){
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }


}