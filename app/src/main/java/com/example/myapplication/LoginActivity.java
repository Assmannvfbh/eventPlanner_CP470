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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    //Test data: Username = Dominik, Password = testpassword

    Toolbar toolbar;
    EditText username;
    EditText password;
    SQLiteDatabase database;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        username = findViewById(R.id.login_uName_textfield);
        password = findViewById(R.id.login_psw_textfield);
        loginButton = findViewById(R.id.login_login);
        loginButton.setOnClickListener(new loginListener());


        RegisterService service = new RegisterService(this);
        database = service.getWritableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.login_about:
                Snackbar.make(this.toolbar, getResources().getString(R.string.login_about_Niklas), Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_toolbar_signIn:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public class loginListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String usn = username.getText().toString();
            String psw = password.getText().toString();
            String[] parameters = new String[2];
            parameters[0] = usn;
            parameters[1] = psw;

            Cursor cursor = database.rawQuery("SELECT * FROM " + RegisterService.TABLE_NAME + " WHERE USERNAME = ? AND PASSWORD = ?",parameters);
            cursor.moveToFirst();

            if(cursor.getCount() > 0){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "Wrong Password or Username does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }




}