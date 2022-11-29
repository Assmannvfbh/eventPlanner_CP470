package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView profile;
    ImageView Event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        profile = (ImageView) findViewById(R.id.profileIcon);
        Event = (ImageView) findViewById(R.id.EventIcon);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(createEvent);
            }
        });
        //here
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