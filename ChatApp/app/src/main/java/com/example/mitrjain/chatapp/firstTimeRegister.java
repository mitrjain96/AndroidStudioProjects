package com.example.mitrjain.chatapp;

import android.content.Intent;
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

public class firstTimeRegister extends AppCompatActivity {
    Cursor resultSet;
    static SQLiteDatabase db;
    Intent i;
    EditText fullName,regisTok,emailid,passwd,repasswd;
    static String refreshedToken="";
    static private int firstTimeFlag=0;
    static private String userContact="";
    private DatabaseReference mDatabase;
    private DatabaseReference ref;

    static void setRefreshedToken(String value)
    {
        refreshedToken=value;
        if(firstTimeFlag==1)
        {
            db.execSQL("UPDATE USER_DETAILS SET deviceToken='"+refreshedToken+"' WHERE Contact ='"+userContact+"'");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase= FirebaseDatabase.getInstance().getReference("Users");

        db = openOrCreateDatabase("ChatApp",MODE_PRIVATE,null);
        i = new Intent(getBaseContext(),MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        resultSet=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name = 'USER_DETAILS'",null);
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
            resultSet=db.rawQuery("SELECT * from USER_DETAILS",null);
            resultSet.moveToFirst();
            String name=resultSet.getString(0);
            String contact=resultSet.getString(1);
            String email=resultSet.getString(2);
            String key=resultSet.getString(3);
            refreshedToken=FirebaseInstanceId.getInstance().getToken();
            userDetails user = new userDetails(name,contact,key,email,refreshedToken);
            mDatabase.child(contact).setValue(user);
            Log.d("Networking",name);
            i.putExtra("User_Name",name);
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
        if(test.length()!=10 || !test.matches("[\\d]{10}"))
        {
            Toast.makeText(this, "Contact Number length cannot must be equal to 10 digits and should contain only digits", Toast.LENGTH_SHORT).show();
            return;
        }
        token=test;
        test=emailid.getText().toString();
        if(!test.matches(".*@.*.com"))
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
        db.execSQL("CREATE TABLE IF NOT EXISTS USER_DETAILS(Name VARCHAR, Contact VARCHAR, Email_id VARCHAR, Key VARCHAR, deviceToken VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS OTHER_USERS(Name VARCHAR, Contact VARCHAR, deviceToken VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CurrentChat(Name VARCHAR, Contact Varchar, deviceToken VARCHAR)");
        firstTimeFlag=1;
        userContact=token;
        try {
            refreshedToken=FirebaseInstanceId.getInstance().getToken();
            db.execSQL("INSERT INTO USER_DETAILS VALUES('"+name+"','"+token+"','"+email+"','"+key+"','"+refreshedToken+"');");
        }catch(Exception e){Log.d("Networking frm.java","Unable to enter in local Database");}
        while(refreshedToken==null)
        {
            refreshedToken=FirebaseInstanceId.getInstance().getToken();
        }
        userDetails user = new userDetails(name,token,key,email,refreshedToken);
        mDatabase.child(token).setValue(user);
        i.putExtra("User_Name",name);
        startActivity(i);
    }

}
