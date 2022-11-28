package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

//import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.MapView;

import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {
    EditText eventTitle;
    EditText eventOrganizer;
    EditText eventDescription;
    TextView eventDate;
    TextView eventTime;
    EditText eventPrice;
    MapView map;
    Dialog dialog;
    Toolbar toolbar;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventTitle = (EditText) findViewById(R.id.eventTitleText);
        eventOrganizer = (EditText) findViewById(R.id.eventOrganizerText);
        eventDescription = (EditText) findViewById(R.id.eventDescriptionText);
        eventDate = (TextView) findViewById(R.id.editTextDate);
        eventDate.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog();
            }
        });
        eventTime = (TextView) findViewById(R.id.editTextTime);
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTimeDialog();
            }
        });
        eventPrice = (EditText) findViewById(R.id.priceText);
        map = findViewById(R.id.mapView);

        EventService service = new EventService(this);
        db = service.getWritableDatabase();
        service.onCreate(db);

        toolbar = findViewById(R.id.createEvent_toolbar);
        setSupportActionBar(toolbar);

    }

    private void createTimeDialog() {
        final Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                boolean isPM = hourOfDay > 12;
                if(isPM)
                    hourOfDay-=12;
                String minute_text = minute<10 ? "0" +String.valueOf(minute):String.valueOf(minute);
                eventTime.setText(createTime(String.valueOf(hourOfDay), minute_text, (isPM ? " pm": "am")));
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public String createTime(String hour, String minute, String am_or_pm){
        return hour + ":" + minute + am_or_pm;
    }
    private void createDateDialog() {
        dialog = new Dialog(CreateEvent.this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialog.setContentView(inflater.inflate(R.layout.datepicker,null));
        DatePicker datePicker = dialog.findViewById(R.id.dateicker_datepciker);
        Button ok_button = dialog.findViewById(R.id.datepicker_ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateText(datePicker);
            }
        });

        ok_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setDateText(datePicker);
            }
        });
        dialog.show();
    }
    public void setDateText(DatePicker datePicker){
        int month = datePicker.getMonth() + 1; //add +1, because getMonth() returns values from 0 - 11
        String st = datePicker.getYear() + "-" + month + "-" + datePicker.getDayOfMonth();
        eventDate.setText(st);
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void createEvent(View view){
        Map<String, String> map = new HashMap<>();
        map.put("title", eventTitle.getText().toString());
        map.put("organizer", eventOrganizer.getText().toString());
        map.put("description", eventDescription.getText().toString());
        map.put("time", eventTime.getText().toString());
        map.put("date", eventDate.getText().toString());
        map.put("price", eventPrice.getText().toString());

        EventQuery eventQuery = new EventQuery();
        eventQuery.execute(map);


    }

    public class EventQuery extends AsyncTask<Map<String, String>, Integer, Integer> {

        @Override
        protected Integer doInBackground(Map<String, String>... maps) {
            Map<String, String> entries = maps[0];
            ContentValues contentValues = new ContentValues();
            contentValues.put(EventService.TITLE, entries.get("title"));
            contentValues.put(EventService.ORGANIZER, entries.get("organizer"));
            contentValues.put(EventService.DESCRIPTION, entries.get("description"));
            contentValues.put(EventService.LOCATION, entries.get("location"));
            contentValues.put(EventService.PRICE, entries.get("price"));
            contentValues.put(EventService.DATE, entries.get("date"));
            contentValues.put(EventService.TIME, entries.get("time"));

            try {
                db.insert(EventService.TABLE_NAME, null, contentValues);
                return 1;
            } catch (Exception e) {
                Log.e(this.getClass().getName(), e.getMessage());
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer){
            super.onPostExecute(integer);
            if(integer==1){
                finish();
            }
        }
    }
}