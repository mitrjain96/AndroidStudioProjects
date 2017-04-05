package com.example.mitrjain.chatapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

/**
 * Created by Mit R Jain on 02-04-2017.
 */

public class firstTimeRegister extends AppCompatActivity {
    Cursor resultSet;
    SQLiteDatabase db;
    Intent i;
    EditText fullName,regisTok,emailid,passwd,repasswd;
    Random rn;
    static String refreshedToken="";
    private DatabaseReference mDatabase;

    static void setRefreshedToken(String value)
    {
        refreshedToken=value;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        db = openOrCreateDatabase("ChatApp",MODE_PRIVATE,null);
        i = new Intent(getBaseContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        resultSet=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name = 'REGIS_TOKEN'",null);
        if(resultSet.getCount()==0)
        {
            Log.d("Networking fRM.java","Fresh Connection");
            setContentView(R.layout.first_time_register);
            fullName=(EditText)findViewById(R.id.fullName);
            regisTok=(EditText)findViewById(R.id.regisToken);
            emailid=(EditText)findViewById(R.id.emailid);
            passwd=(EditText)findViewById(R.id.passwd);
            repasswd=(EditText)findViewById(R.id.repasswd);

        }
        else
        {

            resultSet=db.rawQuery("Select * from REGIS_TOKEN",null);
            resultSet.moveToFirst();
            String token=resultSet.getString(0);
            Log.d("Networking frm.java","Previous token used - "+token);
            resultSet=db.rawQuery("SELECT * from USER_DETAILS",null);
            resultSet.moveToFirst();
            String name=resultSet.getString(0);
            String contact=resultSet.getString(1);
            String email=resultSet.getString(2);
            String key=resultSet.getString(3);
            userDetails user = new userDetails(name,contact,key,email,refreshedToken);
            mDatabase.child(token).setValue(user);
            startActivity(i);
        }

    }
    public void validateForm(View view)
    {
        if(fullName.getText().toString().trim().equals("")||regisTok.getText().toString().trim().equals("")||emailid.getText().toString().trim().equals("")||passwd.getText().toString().trim().equals("")||repasswd.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Do not leave any fields blank", Toast.LENGTH_SHORT).show();
         return;
        }

        String token=regisTok.getText().toString();
        String name,email,key;
        String test=fullName.getText().toString();

        if(test.matches(".*\\d.*") || test.trim().equals(""))
        {
            Toast.makeText(this, "Name must not contain any digits", Toast.LENGTH_SHORT).show();
            return;
        }
        name=test;
        test=regisTok.getText().toString();
        if(test.length()>10 || !test.matches("[\\d]{10}"))
        {
            Toast.makeText(this, "Contact Number length cannot be more than 10 digits and should contain only digits", Toast.LENGTH_SHORT).show();
            return;
        }
        token=test;
        test=emailid.getText().toString();
        if(!test.matches(".*@.*"))
        {
            Toast.makeText(this, "Enter valid Email-id", Toast.LENGTH_SHORT).show();
            return;
        }
        email=test;
        test=passwd.getText().toString();
        String test1=repasswd.getText().toString();
        if(!test.equals(test1))
        {
            Toast.makeText(this, "The entered passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        key=test;
        db.execSQL("CREATE TABLE IF NOT EXISTS REGIS_TOKEN(Token VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS USER_DETAILS(Name VARCHAR, Token VARCHAR, Email_id VARCHAR, Key VARCHAR);");
       // new Authenticate().execute(token);
        try {
            db.execSQL("INSERT INTO REGIS_TOKEN VALUES('"+token+"');");
            Log.d("networking frm.java",name.toString()+" "+email.toString()+" "+token.toString());
            db.execSQL("INSERT INTO USER_DETAILS VALUES('"+name+"','"+token+"','"+email+"','"+key+"');");
        }catch(Exception e){Log.d("Networking frm.java","Unable to enter in local Database");}
        userDetails user = new userDetails(name,token,key,email,refreshedToken);
        mDatabase.child(token).setValue(user);
        startActivity(i);
    }

}
