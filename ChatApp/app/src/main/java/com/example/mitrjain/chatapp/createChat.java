
        package com.example.mitrjain.chatapp;

        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.Toast;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;


/**
 * Created by Mit R Jain on 07-04-2017.
 */

public class createChat extends AppCompatActivity {
    EditText editText;
    SQLiteDatabase db;
    Cursor resultSet;
    static String userContact="";
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    String contact="";
    class dataBaseQuery extends AsyncTask<String,Integer,Integer> {
        int flag=1;
        DatabaseReference mref;
        String userInfo="";

        protected void onPreExecute()
        {

            db =openOrCreateDatabase("ChatApp",MODE_PRIVATE,null);
        }

        protected Integer doInBackground(String... params)
        {
            mref= FirebaseDatabase.getInstance().getReference("Users").child(params[0]);
            mref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.hasChild("refreshedToken"))
                    {
                        flag=0;
                        return;
                    }

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        userInfo=userInfo.concat(dataSnapshot1.getValue(String.class)+" ");
                    }
                    userDetails recuser = new userDetails(userInfo);
                    Log.d("Networking userDetails",userInfo);
                    db.execSQL("INSERT INTO OTHER_USERS VALUES('" + recuser.name + "','" + recuser.contact_no + "','" + recuser.refreshedToken + "')");
                    db.execSQL("DELETE FROM CurrentChat");
                    db.execSQL("INSERT INTO CurrentChat VALUES('" + recuser.name + "','" + recuser.contact_no + "','" + recuser.refreshedToken + "')");
                    db.execSQL("CREATE TABLE IF NOT EXISTS Messages"+recuser.contact_no+"(Message VARCHAR)");
                    MainActivity.returnFlag = true;
                    MainActivity.currentChatUserName=recuser.name;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            int i = 0;
            while(MainActivity.returnFlag==false)
            {
                if(flag==0)
                    break;
                i=(i+10)%100;
                publishProgress(i);
            }
            return flag;

        }
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
        protected void onPostExecute(Integer result)
        {
            progressBar.setVisibility(View.GONE);
            relativeLayout.setClickable(true);
            if(result==0)
            {
                Toast.makeText(getBaseContext(),"The contact number entered isn't a part of the ChatApp Community",Toast.LENGTH_SHORT).show();
            }
            else{
                onBackPressed();
            }
        }




    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_chat);
        editText = (EditText) findViewById(R.id.editText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        relativeLayout=(RelativeLayout) findViewById(R.id.mainChatView);
        progressBar.setMax(100);
        userContact=getIntent().getStringExtra("User_Contact");
    }

    public void newChat(View view)
    {
        contact = editText.getText().toString();
        contact=contact.trim();
        resultSet=db.rawQuery("SELECT * from OTHER_USERS",null);
        while(resultSet.moveToNext())
        {
            if(contact.equals(resultSet.getString(1)))
            {
                Toast.makeText(getBaseContext(), "You have already added this user to your contact list", Toast.LENGTH_SHORT).show();
                return;

            }
        }
        if(contact.equals(userContact))
        {
            Toast.makeText(getBaseContext(),"You cannot enter your own contact number!",Toast.LENGTH_SHORT);
        }
        else {
            new dataBaseQuery().execute(contact);
            relativeLayout.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}