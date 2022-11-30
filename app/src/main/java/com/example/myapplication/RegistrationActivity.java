package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    Toolbar toolbar;
    SQLiteDatabase database;
    EditText usernameTextField;
    EditText emailTextField;
    EditText passwordTextField;
    EditText passwordRepeatTextField;
    EditText nameTextField;
    EditText surnameTextField;
    TextView birthday;
    Dialog dialog;
    ImageView profilePic;
    Button registerButton;
    int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.registration_toolbar);
        setSupportActionBar(toolbar);

        RegisterService service = new RegisterService(this);
        database = service.getWritableDatabase();

        registerButton = findViewById(R.id.registration_register_button);
        registerButton.setOnClickListener(new registerListener());
        usernameTextField = findViewById(R.id.registration_username_input);
        emailTextField = findViewById(R.id.registration_email_input);
        passwordTextField = findViewById(R.id.registration_password_input);
        passwordRepeatTextField = findViewById(R.id.registration_password_input_repeat);
        surnameTextField = findViewById(R.id.registration_surname_input);
        nameTextField = findViewById(R.id.registration_firstname_input);
        birthday = findViewById(R.id.registration_DO2);
        birthday.setOnClickListener(new dateOfBirthListener());

        profilePic = (ImageView) findViewById(R.id.profile_photo);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent takePicture) {
        super.onActivityResult(requestCode, resultCode, takePicture);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = takePicture.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(imageBitmap);
        }
    }

    private void createDialog() {
        dialog = new Dialog(RegistrationActivity.this);
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
        birthday.setText(st);
        dialog.dismiss();
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
            case R.id.register1_help:
                Snackbar.make(this.toolbar, getResources().getString(R.string.register1_about_Niklas), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public class registerListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //Creating shared preference
            SharedPreferences sharedpref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedpref.edit();
            myEdit.putString("username", usernameTextField.getText().toString());
            myEdit.putString("email", emailTextField.getText().toString());
            myEdit.putString("password", passwordTextField.getText().toString());
            myEdit.putString("passwordRepeat", passwordRepeatTextField.getText().toString());
            myEdit.putString("forename", nameTextField.getText().toString());
            myEdit.putString("surname", surnameTextField.getText().toString());
            myEdit.putString("dateOfBirth", birthday.getText().toString());
            myEdit.commit();

            //TODO: Check Email field for duplicates, password parity validation
            Map<String, String> entries = new HashMap<>();

            entries.put("username", usernameTextField.getText().toString());
            entries.put("email", emailTextField.getText().toString());
            entries.put("password", passwordTextField.getText().toString());
            entries.put("passwordRepeat", passwordRepeatTextField.getText().toString());
            entries.put("forename", nameTextField.getText().toString());
            entries.put("surname", surnameTextField.getText().toString());
            entries.put("dateOfBirth", birthday.getText().toString());

            RegistrationQuery query = new RegistrationQuery();
            query.execute(entries);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    public class dateOfBirthListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            createDialog();
        }


    }

    public class RegistrationQuery extends AsyncTask<Map<String, String>, Integer, Integer> {

        final Integer USERNAME_EXISTS = -1;
        final Integer USER_CREATED = 1;
        final Integer ENTRY_ERROR = -2;


        @Override
        protected Integer doInBackground(Map<String, String>... maps) {
            Map<String, String> entries = maps[0];
            String[] usernameArray = new String[1];
            usernameArray[0] = entries.get("username");

            Cursor cursorForUsername = database.rawQuery("Select USERNAME from " + RegisterService.TABLE_NAME + " WHERE USERNAME = ?", usernameArray);
            int rows = cursorForUsername.getCount();
            if (rows > 0) {
                return USERNAME_EXISTS;
            }
            else if (android.util.Patterns.EMAIL_ADDRESS.matcher(entries.get("email")).matches() && entries.get("password").equals(entries.get("passwordRepeat"))) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(RegisterService.USERNAME, entries.get("username"));
                contentValues.put(RegisterService.EMAIL, entries.get("email"));
                contentValues.put(RegisterService.PASSWORD, entries.get("password"));
                contentValues.put(RegisterService.DATE_OF_BIRTH, entries.get("dateOfBirth"));
                contentValues.put(RegisterService.NAME, entries.get("forename"));
                contentValues.put(RegisterService.SURNAME, entries.get("surname"));

                database.insert(RegisterService.TABLE_NAME, null, contentValues);
                return USER_CREATED;
            }
            return ENTRY_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer.equals(USER_CREATED)) {

                //Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.registration_suc), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else if(integer.equals(ENTRY_ERROR)){

                //Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.registration_pass_invalid), Toast.LENGTH_SHORT).show();
            }
            else if(integer.equals(USERNAME_EXISTS)){
                //Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.registration_usern_exists), Toast.LENGTH_SHORT).show();

            }
        }


    }
}