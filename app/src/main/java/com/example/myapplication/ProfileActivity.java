package com.example.myapplication;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends RegistrationActivity {
    Button updateButton;
    EditText name_inp;
    EditText lastName_inp;
    TextView user;
    EditText eMail_inp;
    EditText pass;
    EditText passrepeat;
    TextView DOB;
    Dialog helpDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DatabaseService service = new DatabaseService(this);
        updateButton = (Button) findViewById(R.id.update_button);
        name_inp = (EditText) findViewById(R.id.registration_firstname_input);
        lastName_inp = (EditText) findViewById(R.id.registration_surname_input);
        user = (TextView) findViewById(R.id.registration_username_input);
        eMail_inp = (EditText) findViewById(R.id.registration_email_input);
        pass = (EditText) findViewById(R.id.registration_password_input);
        passrepeat = (EditText) findViewById(R.id.registration_password_input_repeat);
        DOB = (TextView) findViewById(R.id.registration_DO2);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        toolbar = this.findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        String name = sh.getString("forename", "");
        String surname = sh.getString("surname", "");
        String username = sh.getString("username", "");
        String email = sh.getString("email", "");
        String password = sh.getString("password", "");
        String passwordRepeat = sh.getString("passwordRepeat", "");
        String date = sh.getString("dateOfBirth", "");

        name_inp.setText(name);
        lastName_inp.setText(surname);
        user.setText(username);
        eMail_inp.setText(email);
        pass.setText(password);
        passrepeat.setText(passwordRepeat);
        DOB.setText(date);

        DatabaseService service1 = new DatabaseService(this);

        database = service1.getWritableDatabase();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String query = "UPDATE " + RegisterService.TABLE_NAME + " SET username= '" + user.getText().toString()
//                        +"', password= '" + pass.getText().toString() + "', dateOfBirth= '"+ DOB.getText().toString()
//                        + "', email= '" + eMail_inp.getText().toString() + "' WHERE forename= '" + name_inp.getText().toString()
//                        + "' AND surname= '" + lastName_inp.getText().toString() + "';";
                //database.execSQL(query);
                ContentValues values = new ContentValues();
                values.put("PASSWORD", pass.getText().toString());
                values.put("DATE_OF_BIRTH", DOB.getText().toString());
                values.put("EMAIL", eMail_inp.getText().toString());
                values.put("NAME", name_inp.getText().toString());
                values.put("SURNAME", lastName_inp.getText().toString());
                database.update(DatabaseService.TEST_TABLE_NAME, values , "USERNAME = ?", new String[]{username});
                //SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.putString("username", user.getText().toString());
                myEdit.putString("email", eMail_inp.getText().toString());
                myEdit.putString("password", pass.getText().toString());
                myEdit.putString("passwordRepeat", passrepeat.getText().toString());
                myEdit.putString("forename", name_inp.getText().toString());
                myEdit.putString("surname", lastName_inp.getText().toString());
                myEdit.putString("dateOfBirth", DOB.getText().toString());
                myEdit.commit();

                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String name = sh.getString("forename", "");
        String surname = sh.getString("surname", "");
        String username = sh.getString("username", "");
        String email = sh.getString("email", "");
        String password = sh.getString("password", "");
        String passwordRepeat = sh.getString("passwordRepeat", "");
        String date = sh.getString("dateOfBirth", "");
        name_inp.setText(name);
        lastName_inp.setText(surname);
        user.setText(username);
        eMail_inp.setText(email);
        pass.setText(password);
        passrepeat.setText(passwordRepeat);
        DOB.setText(date);
    }

    public void openHelpDialog() {
        helpDialog = new Dialog(this);
        helpDialog.setContentView(R.layout.dialog_help);
        Button okButton = helpDialog.findViewById(R.id.help_dialog_ok);
        TextView text = helpDialog.findViewById(R.id.help_dialog_text);

        text.setText(getResources().getString(R.string.help_dialog_profile));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
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
                openHelpDialog();
                break;

            case R.id.menu_standard_logout:
                UserData.getUserData().clear();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

        return true;
    }
}