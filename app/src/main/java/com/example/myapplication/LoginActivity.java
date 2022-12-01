package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Test data: Username = assmannvfbh, Password = abc
    //Test data: Username = Marcus, Password = abc
    Toolbar toolbar;
    EditText username;
    EditText password;
    SQLiteDatabase database;
    Button loginButton;
    ProgressBar progressBar;
    UserData userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        username = findViewById(R.id.login_uName_textfield);
        password = findViewById(R.id.login_psw_textfield);
        progressBar = findViewById(R.id.login_progressBar);
        loginButton = findViewById(R.id.login_login);
        loginButton.setOnClickListener(new loginListener());


        DatabaseService service = new DatabaseService(this);
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
            progressBar.setVisibility(View.VISIBLE);
            Map<String,String> entries = new HashMap<>();
            entries.put("username",username.getText().toString());
            entries.put("password",password.getText().toString());

            LoginQuery query = new LoginQuery();
            query.execute(entries);
        }
    }

    public class LoginQuery extends AsyncTask<Map<String, String>, Integer, Integer>{

        final Integer LOGIN_SUCESSFULL = 1;
        final Integer LOGIN_FAILURE = -1;


        @Override
        protected Integer doInBackground(Map<String, String>... maps) {

            Map<String, String> entries = maps[0];
            String[] parameters = new String[2];
            parameters[0] = entries.get("username");
            parameters[1] = entries.get("password");

            Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseService.TEST_TABLE_NAME + " WHERE USERNAME = ? AND PASSWORD = ?",parameters);
            cursor.moveToFirst();
            publishProgress(25);

            if(cursor.getCount() > 0){
                //populating UserData singleton
                UserData.getUserData().setUsername(cursor.getString(0));
                UserData.getUserData().setEmail(cursor.getString(1));
                UserData.getUserData().setName(cursor.getString(3));
                publishProgress(50);
                UserData.getUserData().setSurname(cursor.getString(4));
                UserData.getUserData().setDateOfBirth(cursor.getString(5));
                publishProgress(100);

                String string = UserData.getUserData().getUsername();
                return LOGIN_SUCESSFULL;
            }
            else {
                return LOGIN_FAILURE;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer.equals(LOGIN_SUCESSFULL)){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else if(integer.equals(LOGIN_FAILURE)){
                Toast.makeText(LoginActivity.this, "Wrong Password or Username does not exist", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }




}