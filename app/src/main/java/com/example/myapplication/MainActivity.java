package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView profile;
    ImageView myEvents;

    ImageView party_list;

    ImageView map;
    Button eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        profile = (ImageView) findViewById(R.id.profileIcon);
        myEvents = (ImageView) findViewById(R.id.main_my_events_icon);

        party_list = (ImageView) findViewById(R.id.homeIcon);

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
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }




    public void setupLayout(){
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }


}