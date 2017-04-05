package com.example.mitrjain.chatapp;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.*;

import com.example.mitrjain.chatapp.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    Button bt;
    SQLiteDatabase db;
    static TextView textView;
    static String recTok="Nan";
    Random rn;
    Thread t;
    public static ListView listView;
    public static ListAdapter listAdapter;
    ArrayList<String> content= new ArrayList<String>();
    static int z=0;
    static String newMesg="";

    static class updateListView extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params)
        {

            Log.d("Networking Update",params[0]);
            return params[0];
        }
        protected void onPostExecute(String message)
        {
            listAdapter.add("#REC"+message);
            listAdapter.notifyDataSetChanged();
        }


    }
    static void newMessageReceived(String val)
    {
        new updateListView().execute(val);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView)findViewById(R.id.LV1);
        listAdapter = new ListAdapter(this,R.layout.smessage,content);
        listView.setAdapter(listAdapter);
        ed=(EditText)findViewById(R.id.ed1);
        bt=(Button)findViewById(R.id.bt1);
        String token=getIntent().getStringExtra("Registraion_token");
    }
    public void onStart()
    {

        super.onStart();
        Log.d("Networking","In On Start");
        Log.d("Networking Token", FirebaseInstanceId.getInstance().getToken());
    }


    public void startSocket(View view)
    {

        CharSequence str = ed.getText();
        String message = str.toString();
        Log.d("Networking",message);
        listAdapter.add("#SEN"+message);
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        new sendMessage().execute(message,deviceToken);
        ed.setText("");



    }


}

