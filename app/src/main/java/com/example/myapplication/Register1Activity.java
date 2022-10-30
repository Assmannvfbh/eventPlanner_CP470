package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Register1Activity extends AppCompatActivity {

    Toolbar toolbar;
    SQLiteDatabase database;
    EditText usernameTextField;
    EditText emailTextField;
    EditText passwordTextField;
    EditText passwordRepeatTextField;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        toolbar = findViewById(R.id.register1_toolbar);
        setSupportActionBar(toolbar);

        RegisterService service = new RegisterService(this);
        database = service.getWritableDatabase();

        registerButton = findViewById(R.id.register1_button);
        registerButton.setOnClickListener(new registerListener());
        usernameTextField = findViewById(R.id.register1_username_Input);
        emailTextField = findViewById(R.id.register1_email_Input);
        passwordTextField = findViewById(R.id.register1_password_Input);
        passwordRepeatTextField = findViewById(R.id.register1_passwordRepeat_Input);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register1_menu,menu);
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
            String[] usernameArray = new String[1];
            usernameArray[0] = username;
            Cursor cursorForUsername = database.rawQuery("Select USERNAME from " + RegisterService.TABLE_NAME + " WHERE USERNAME = ?" , usernameArray);
            int rows = cursorForUsername.getCount();
            if(rows > 0){
                Toast.makeText(Register1Activity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(RegisterService.USERNAME, username);
                contentValues.put(RegisterService.EMAIL, email);
                contentValues.put(RegisterService.PASSWORD, password);

                database.insert(RegisterService.TABLE_NAME, null, contentValues);

                Toast.makeText(Register1Activity.this, "Registering successful!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }



}