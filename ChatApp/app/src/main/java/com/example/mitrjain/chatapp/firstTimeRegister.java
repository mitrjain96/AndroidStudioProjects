package com.example.mitrjain.chatapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

public class firstTimeRegister extends AppCompatActivity {
    static String refreshedToken = "";
    Cursor resultSet;
    SQLiteDatabase db;
    Intent i;
    EditText fullName, contactNo, emailid, passwd, repasswd;
    Random rn;
    ProgressBar progressBar;
    LinearLayout registrationInterface;
    private DatabaseReference mDatabase;

    static void setRefreshedToken(String value) {
        refreshedToken = value;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        db = openOrCreateDatabase("ChatApp", MODE_PRIVATE, null);
        i = new Intent(getBaseContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        resultSet = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name = 'USER_DETAILS'", null);
        if (resultSet.getCount() == 0) {
            Log.d("Networking FTR.java", "New User");
            setContentView(R.layout.first_time_register);
            fullName = (EditText) findViewById(R.id.fullName);
            contactNo = (EditText) findViewById(R.id.contactNo);
            emailid = (EditText) findViewById(R.id.emailid);
            passwd = (EditText) findViewById(R.id.passwd);
            repasswd = (EditText) findViewById(R.id.repasswd);
            progressBar = (ProgressBar) findViewById(R.id.registrationProgress);
            registrationInterface = (LinearLayout) findViewById(R.id.registrationInterface);

        } else {
            Log.d("Networking FTR.java", "Existing User");
            resultSet = db.rawQuery("SELECT * from USER_DETAILS", null);
            resultSet.moveToFirst();
            String name = resultSet.getString(0);
            String contact = resultSet.getString(1);
            String email = resultSet.getString(2);
            String key = resultSet.getString(3);
            int progress = 10;
            progressBar.setVisibility(View.VISIBLE);
            registrationInterface.setVisibility(View.INVISIBLE);
            while (refreshedToken.equals("")) {
                progressBar.setMax(100);
                progressBar.setProgress(progress);
                progress = (progress + 30) % 100;
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            }

            Log.d("Networking FTR.java", "Reg Token=" + refreshedToken);
            userDetails user = new userDetails(name, contact, key, email, refreshedToken);
            mDatabase.child(contact).setValue(user);
            i.putExtra("User_Name",name);
            i.putExtra("User_Contact",contact);
            startActivity(i);
        }

    }

    public void validateForm(View view) {
        if (fullName.getText().toString().trim().equals("") || contactNo.getText().toString().trim().equals("") || emailid.getText().toString().trim().equals("") || passwd.getText().toString().trim().equals("") || repasswd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Do not leave any fields blank", Toast.LENGTH_SHORT).show();
            return;
        }


        String name, email, key, contact;
        name = fullName.getText().toString();
        name = name.trim();
        if (name.matches(".*\\d.*") || name.equals("")) {
            Toast.makeText(this, "Name must not contain any digits", Toast.LENGTH_SHORT).show();
            fullName.setBackgroundColor(Color.RED);
            return;
        }
        contact = contactNo.getText().toString();
        contact = contact.trim();
        if (contact.length() > 10 || !contact.matches("[\\d]{10}")) {
            Toast.makeText(this, "Contact Number length cannot be more than 10 digits and should contain only digits", Toast.LENGTH_SHORT).show();
            contactNo.setBackgroundColor(Color.RED);
            return;
        }

        email = emailid.getText().toString();
        email = email.trim();
        if (!email.matches(".*@.*")) {
            Toast.makeText(this, "Enter valid Email-id", Toast.LENGTH_SHORT).show();
            emailid.setBackgroundColor(Color.RED);
            return;
        }
        key = passwd.getText().toString();
        String key1 = repasswd.getText().toString();
        if (!key.equals(key1)) {
            Toast.makeText(this, "The entered passwords do not match", Toast.LENGTH_SHORT).show();
            passwd.setBackgroundColor(Color.RED);
            repasswd.setBackgroundColor(Color.RED);
            return;

        }
        db.execSQL("CREATE TABLE IF NOT EXISTS USER_DETAILS(Name VARCHAR, Contact VARCHAR, Email_id VARCHAR, Key VARCHAR, deviceToken VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS OTHER_USERS(Name VARCHAR, Contact VARCHAR, deviceToken VARCHAR)");


        int progress = 10;
        registrationInterface.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        fullName.setBackgroundColor(Color.GREEN);
        contactNo.setBackgroundColor(Color.GREEN);
        emailid.setBackgroundColor(Color.GREEN);
        passwd.setBackgroundColor(Color.GREEN);
        repasswd.setBackgroundColor(Color.GREEN);
        registrationInterface.setVisibility(View.INVISIBLE);
        while (refreshedToken.equals("")) {

            progressBar.setMax(100);
            progressBar.setProgress(progress);
            progress = (progress + 30) % 100;
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
        }
        Log.d("Networking FTR.java", "Reg Token=" + refreshedToken);
        try {
            db.execSQL("INSERT INTO USER_DETAILS VALUES('" + name + "','" + contact + "','" + email + "','" + key + "','" + refreshedToken + "');");
        } catch (Exception e) {
            Log.d("Networking FTR.java", "Unable to enter in local Database");
        }
        userDetails user = new userDetails(name, contact, key, email, refreshedToken);
        mDatabase.child(contact).setValue(user);
        i.putExtra("User_Name",name);
        i.putExtra("User_Contact",contact);
        startActivity(i);
    }

}
