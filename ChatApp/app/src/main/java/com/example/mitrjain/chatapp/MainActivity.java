package com.example.mitrjain.chatapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    Button bt;
    static FloatingActionButton fab;
    static TextView initialText,currentUser,currentChatName;
    static DrawerLayout navDrawer;
    ListView messageInterface, ContactListView;
    LinearLayout parentMessageInterface;
    static String userName="";
    static String contact="";
    static String details="";
    static  String currentChatUserName="";
    public static ListAdapter listAdapter;
    public static ArrayAdapter<String> contactsListAdapter;
    ArrayList<String> messageList= new ArrayList<String>();
    ArrayList<String> contactList = new ArrayList<String>();
    static private SQLiteDatabase db;
    Cursor resultSet;
    static String rec="";
    static boolean returnFlag=false;
    private DatabaseReference mDatabase;
    static boolean refreshFlag = true;
    static DatabaseReference mref;
    static class updateListView extends AsyncTask<String,Void,Integer>
    {
        ArrayList<String> users = new ArrayList<String>();
        Cursor resultSet;
        String splitting[];
        String recContact;
        String recName="";
        @Override
        protected Integer doInBackground(String... params)
        {

            //message-contact-name
            splitting = params[0].split("-");
            recContact=splitting[1];
            resultSet=db.rawQuery("SELECT * FROM OTHER_USERS WHERE Contact='"+recContact+"'",null);
            while(resultSet.moveToNext())
            {
                users.add(resultSet.getString(1));
            }
            Log.d("Networking here1",splitting[2]);
            Log.d("Networking here1",currentChatUserName);
            if(splitting[2].equalsIgnoreCase(currentChatUserName))
            {
                db.execSQL("INSERT INTO Messages"+contact+" VALUES('#REC"+splitting[0]+"')");
                Log.d("Networking ", "Here 1");
                return new Integer(0);
            }
            else if(users.size()==0 || !(users.contains(recContact)))
            {
                Log.d("Networking ", "Here 2");
                db.execSQL("CREATE TABLE IF NOT EXISTS Messages"+recContact+"(Message VARCHAR)");
                db.execSQL("INSERT INTO Messages"+recContact+" VALUES('#REC"+splitting[0]+"')");
                mref= FirebaseDatabase.getInstance().getReference("Users").child(recContact);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild("refreshedToken"))
                        {
                            return;
                        }
                        String userInfo="";
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            userInfo=userInfo.concat(dataSnapshot1.getValue(String.class)+" ");
                        }
                        userDetails recuser = new userDetails(userInfo);

                        db.execSQL("INSERT INTO OTHER_USERS VALUES('" + recuser.name + "','" + recuser.contact_no + "','" + rec + "')");
                        recName=recuser.name;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                return new Integer(-1);

            }
            else if(users.contains(recContact))
            {
                Log.d("Networking ", "Here 3");
                db.execSQL("INSERT INTO Messages"+recContact+" VALUES('#REC"+splitting[0]+"')");
                return 1;
            }
            else {
                Log.d("Networking ", "Here 4");
                return 2;}
//            Log.d("Networking Update",params[0]);


        }
        protected void onPostExecute(Integer i) {

            if (i == 0) {
                Log.d("Networking recieve:",splitting[0]);
                listAdapter.add("#REC" + splitting[0]);
                listAdapter.notifyDataSetChanged();
            }
            else if(i==1){
                //TODO ADD USER NOTIFICATION

            }
            else if(i==-1)
            {
                contactsListAdapter.add(recName);
                contactsListAdapter.notifyDataSetChanged();
               // fab.setVisibility(View.GONE);
                navDrawer.setVisibility(View.VISIBLE);

            }
        }


    }
    static void newMessageReceived(String val){ new updateListView().execute(val); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = openOrCreateDatabase("ChatApp",MODE_PRIVATE,null);
        userName=getIntent().getStringExtra("User_Name");
        contact=getIntent().getStringExtra("User_Contact");
        resultSet=db.rawQuery("Select * FROM OTHER_USERS",null);
        setContentView(R.layout.navigation_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageInterface = (ListView)findViewById(R.id.messageInterface);
        ContactListView = (ListView)findViewById(R.id.ContactsListView);
        ed=(EditText)findViewById(R.id.ed1);
        bt=(Button)findViewById(R.id.bt1);
        //fab=(FloatingActionButton)findViewById(R.id.fab);
        initialText=(TextView)findViewById(R.id.initialTextView);
        currentChatName=(TextView)findViewById(R.id.currentChatName);
        navDrawer=(DrawerLayout) findViewById(R.id.parentMessageInterface);
        parentMessageInterface=(LinearLayout)findViewById(R.id.parentMessageInterface);
        listAdapter=new ListAdapter(this,R.layout.smessage,messageList);
        contactsListAdapter=new ArrayAdapter<String>(this,R.layout.smessage,contactList);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();
        registerForContextMenu(ContactListView);
        ContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView tv = (TextView)v;
                if(tv.getText().toString()==currentChatUserName) {
                    navDrawer.closeDrawers();
                    return;
                }
                currentChatUserName=tv.getText().toString();
                resultSet=db.rawQuery("Select * FROM OTHER_USERS Where Name='"+currentChatUserName+"'",null);
                while(resultSet.moveToNext())
                {
                    contact=resultSet.getString(1);
                    rec=resultSet.getString(2);
                }

                currentChatName.setText(currentChatUserName);
                listAdapter.clear();
                resultSet=db.rawQuery("SELECT * FROM Messages"+contact+"",null);
                while(resultSet.moveToNext())
                {
                    listAdapter.add(resultSet.getString(0));

                }
                listAdapter.notifyDataSetChanged();
                db.execSQL("DELETE FROM CurrentChat");
                db.execSQL("INSERT INTO CurrentChat VALUES('" + currentChatUserName + "','" + contact + "','" +rec + "')");
                parentMessageInterface.setVisibility(View.VISIBLE);
                messageInterface.setVisibility(View.VISIBLE);
                //fab.setVisibility(View.GONE);
                navDrawer.closeDrawers();


            }
        });

    }
    public void onStart()
    {

        super.onStart();
        Log.d("Networking MainActivity","In On Start");
        if(FirebaseInstanceId.getInstance().getToken()!=null)
            Log.d("Networking Token",FirebaseInstanceId.getInstance().getToken() );
    }
    public void onResume()
    {
        Log.d("Networking MainActivity","In onResume");
        super.onResume();
        resultSet=db.rawQuery("Select * FROM OTHER_USERS",null);
        if(resultSet.getCount()==0 )
        {
            navDrawer.setVisibility(View.GONE);
            parentMessageInterface.setVisibility(View.GONE);
            //fab.setVisibility(View.VISIBLE);
            initialText.setText("Welcome "+userName+".\nLets get started. Click on the Floating Button below to create a new Chat.");
            initialText.setVisibility(View.VISIBLE);
            return;
        }
        if(!returnFlag)
            return;
        if(currentChatUserName.equals(""))
        {
            contactList.clear();
            while(resultSet.moveToNext())
            {
                contactList.add(resultSet.getString(0));
            }
            ContactListView.setAdapter(contactsListAdapter);
            messageInterface.setAdapter(listAdapter);
            parentMessageInterface.setVisibility(View.INVISIBLE);
            navDrawer.setVisibility(View.VISIBLE);
           // fab.setVisibility(View.GONE);
            initialText.setVisibility(View.VISIBLE);
            return;
        }

        resultSet=db.rawQuery("SELECT * FROM CurrentChat",null);
        contact="";
        while(resultSet.moveToNext())
        {
            currentChatUserName=resultSet.getString(0);
            contact=resultSet.getString(1);
            rec=resultSet.getString(2);
        }
        mDatabase=FirebaseDatabase.getInstance().getReference("Users").child(contact);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    details=details.concat(dataSnapshot1.getValue(String.class))+" ";
                }
                // Log.d("Networking user",details);
                //update local Databse


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        contactList.clear();
        resultSet=db.rawQuery("Select * FROM OTHER_USERS",null);
        while(resultSet.moveToNext())
        {

            if(!contactList.contains(resultSet.getString(0)))
                contactList.add(resultSet.getString(0));
        }
        String query="SELECT * FROM Messages"+contact;
        resultSet=db.rawQuery(query,null);
        messageList.clear();
        while(resultSet.moveToNext())
        {
            messageList.add(resultSet.getString(0));
        }
        ContactListView.setAdapter(contactsListAdapter);
        messageInterface.setAdapter(listAdapter);
        initialText.setVisibility(View.GONE);
       // fab.setVisibility(View.GONE);
        navDrawer.setVisibility(View.VISIBLE);
        parentMessageInterface.setVisibility(View.VISIBLE);
        currentChatName.setText(currentChatUserName);
        returnFlag=false;

    }
    public void onStop()
    {
        super.onStop();
        Log.d("networking","In on Stop");
        returnFlag=false;
    }
    public void onPause()
    {
        super.onPause();
        Log.d("networking"," In on Pause");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Networking","In on Destroy");
        returnFlag =true;
    }

    public void onClickFAB(View view)
    {
        Intent i = new Intent(getBaseContext(), createChat.class);
        i.putExtra("User_Contact",contact);
        startActivity(i);

    }


    public void startSocket(View view)
    {

        CharSequence str = ed.getText();
        String message = str.toString();
        Log.d("Networking",message);
        listAdapter.add("#SEN"+message);
        listAdapter.notifyDataSetChanged();
        resultSet=db.rawQuery("SELECT deviceToken FROM OTHER_USERS WHERE Name='"+currentChatUserName+"'",null);
        while(resultSet.moveToNext())
        {
            rec=resultSet.getString(0);
            Log.d("Networking device:",rec);
        }
        Log.d("Networking rec",rec);
        new sendMessage().execute(message,contact,rec,userName);
        db.execSQL("INSERT INTO Messages"+contact+" VALUES('#SEN"+message+"')");
        ed.setText("");



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.ContactsListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_options, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String delname = ((TextView) info.targetView).getText().toString();
        String delContact="";

        switch(item.getItemId()) {
            case R.id.block:
                Toast.makeText(this, "This feature will be available in the next version.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Log.d("Networking:","In delete");
                resultSet = db.rawQuery("SELECT * FROM OTHER_USERS WHERE Name='" + delname + "'", null);
                while (resultSet.moveToNext()) {
                    delContact = resultSet.getString(1);
                }
                db.execSQL("DROP TABLE Messages" + delContact + "; ");
                db.execSQL("DELETE FROM OTHER_USERS WHERE Name='" + delname + "'");
                contactsListAdapter.remove(delname);
                contactsListAdapter.notifyDataSetChanged();
                if(delname.equals(currentChatUserName)) {
                    parentMessageInterface.setVisibility(View.GONE);
                    db.execSQL("DELETE FROM CurrentChat");
                    listAdapter.clear();
                    listAdapter.notifyDataSetChanged();
                    currentChatUserName = "";
                }
                navDrawer.closeDrawers();
                resultSet=db.rawQuery("SELECT * FROM OTHER_USERS",null);
                if(resultSet.getCount()==0)
                {
                    initialText.setVisibility(View.VISIBLE);
                    navDrawer.setVisibility(View.GONE);
                    parentMessageInterface.setVisibility(View.GONE);
                   // fab.setVisibility(View.VISIBLE);
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
            case R.id.newChat:
                Intent i = new Intent(getBaseContext(), createChat.class);
                i.putExtra("User_Contact",contact);
                startActivity(i);
                break;
            default:Log.d("Networking","Default");
        }
        return false;
    }

}