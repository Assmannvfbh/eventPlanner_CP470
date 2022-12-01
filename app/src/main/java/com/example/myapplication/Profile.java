package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends RegistrationActivity {
    Button updateButton;
    EditText name_inp;
    EditText lastName_inp;
    TextView user;
    EditText eMail_inp;
    EditText pass;
    EditText passrepeat;
    TextView DOB;
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "UPDATE " + DatabaseService.TEST_TABLE_NAME + " SET username= '" + user.getText().toString()
                        +"', password= '" + pass.getText().toString() + "', dateOfBirth= '"+ DOB.getText().toString()
                        + "', email= '" + eMail_inp.getText().toString() + "' WHERE forename= '" + name_inp.getText().toString()
                        + "' AND surname= '" + lastName_inp.getText().toString() + "';";
                //database.execSQL(query);
                ContentValues values = new ContentValues();
                values.put("password", pass.getText().toString());
                values.put("dateOfBirth", DOB.getText().toString());
                values.put("email", eMail_inp.getText().toString());
                values.put("forename", name_inp.getText().toString());
                values.put("surname", lastName_inp.getText().toString());
                database.update(DatabaseService.TEST_TABLE_NAME, values , "username="+user.getText().toString(), new String[]{username});
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

                Intent mainIntent = new Intent(Profile.this, MainActivity.class);
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
}