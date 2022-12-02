package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapView extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    protected SQLiteDatabase db;
    protected DatabaseService databaseService;
    private ArrayList<String> partylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        databaseService = new DatabaseService(this);
        db = databaseService.getReadableDatabase();

        //create list of addresses
        partylist = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT LOCATION FROM " + DatabaseService.EVENT_TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            //partylist.add(new Party(cursor.getString(1), cursor.getInt(0)));
            partylist.add(cursor.getString(0));
            cursor.moveToNext();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        List<Address> addressList;
//
//        for(int i = 0; i<partylist.size(); i++){
//            String add = partylist.get(i);
//            try {
//                addressList = geocoder.getFromLocationName(add, 5);
//                if(addressList != null){
//                    Address address = (Address) addressList.get(0);
//                    double lat = address.getLatitude();
//                    double longitude = address.getLongitude();
//
//                    LatLng temp = new LatLng(lat, longitude);
//                    mMap.addMarker(new MarkerOptions()
//                            .position(temp)
//                            .title("Marker in Waterloo"));
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }



        // Add a marker in Sydney and move the camera
        LatLng waterloo = new LatLng(43, -80);
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(waterloo)
                .title("Marker in Waterloo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(waterloo));
    }


}