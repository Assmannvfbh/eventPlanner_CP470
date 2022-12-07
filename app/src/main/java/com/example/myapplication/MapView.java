package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapView extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    Toolbar toolbar;

    protected SQLiteDatabase db;
    protected DatabaseService databaseService;
    private ArrayList<String> partylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        toolbar = this.findViewById(R.id.map_view_toolbar);
        setSupportActionBar(toolbar);
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
                Snackbar.make(this.toolbar, getResources().getString(R.string.register1_about_Niklas), Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_standard_logout:
                UserData.getUserData().clear();
                Intent intent = new Intent(MapView.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        return true;
    }
}