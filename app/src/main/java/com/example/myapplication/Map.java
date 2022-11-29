package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.MapView;

public class Map extends AppCompatActivity {
    boolean isPermissionGranted;
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        checkMyPermission();
    }

    private void checkMyPermission(){

    }
}