package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    Toolbar toolbar;
    SQLiteDatabase database;
    EditText usernameTextField;
    EditText emailTextField;
    EditText passwordTextField;
    EditText passwordRepeatTextField;
    EditText nameTextField;
    EditText surnameTextField;
    EditText birthday;
    Dialog dialog;

    Button registerButton;

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
        birthday.setInputType(InputType.TYPE_NULL);
        birthday.setOnClickListener(new dateOfBirthListener());




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
        String st = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
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

            //TODO: Check Email field for duplicates, password parity validation

            String username = usernameTextField.getText().toString();
            String email = emailTextField.getText().toString();
            String password = passwordTextField.getText().toString();
            String passwordRepeat = passwordRepeatTextField.getText().toString();
            String forename = nameTextField.getText().toString();
            String surname = surnameTextField.getText().toString();
            String date_of_birth = birthday.getText().toString();
            String[] usernameArray = new String[1];
            usernameArray[0] = username;

            Cursor cursorForUsername = database.rawQuery("Select USERNAME from " + RegisterService.TABLE_NAME + " WHERE USERNAME = ?" , usernameArray);
            int rows = cursorForUsername.getCount();
            if(rows > 0){
                Toast.makeText(RegistrationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
            else if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.equals(passwordRepeat)){
                ContentValues contentValues = new ContentValues();
                contentValues.put(RegisterService.USERNAME, username);
                contentValues.put(RegisterService.EMAIL, email);
                contentValues.put(RegisterService.PASSWORD, password);
                contentValues.put(RegisterService.DATE_OF_BIRTH, date_of_birth);
                contentValues.put(RegisterService.NAME,forename);
                contentValues.put(RegisterService.SURNAME,surname);

                database.insert(RegisterService.TABLE_NAME, null, contentValues);

                Toast.makeText(RegistrationActivity.this, "Registering successful!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(RegistrationActivity.this, "Password or Email not valid, please try again", Toast.LENGTH_SHORT).show();
            }
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
}