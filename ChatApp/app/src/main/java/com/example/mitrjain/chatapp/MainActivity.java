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
    class ClientSocket extends AsyncTask<String,Void,String> {
        Socket s=null;
        SocketAddress sockaddr;
        Integer size;
        protected void onPreExecute()
        {
            InetAddress addr;
            try{addr = InetAddress.getByName("192.168.137.1");
                int port = 8888;
                sockaddr = new InetSocketAddress(addr, port);
                Log.d("Networking",addr.toString()+"--"+sockaddr.toString());
            }catch(Exception e){}


        }
        @Override
        protected String doInBackground(String... params)
        {
            try{

                s=new Socket();
                s.connect(sockaddr);


            }

            catch(Exception e){
                Log.d("Networking",e.toString());}
            try {

                if(s!=null) {
                    size=params[0].length();
                    DataOutputStream wr = new DataOutputStream(s.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    wr.writeBytes(size.toString());
                    wr.writeBytes("!");
                    wr.write(params[0].getBytes());
                    wr.flush();
                    s.close();
                }
                else
                {
                    Log.d("Networking","Empty Socket");
                }
            }
            catch(Exception e){Log.d("Networking",e.toString());}

            return params[0];
        }
        protected void onPostExecute(String s) {
            listAdapter.add(s);
            listAdapter.notifyDataSetChanged();
            Log.d("Networking",(new Integer(content.size())).toString());





        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = openOrCreateDatabase("ChatApp",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS REGIS_TOKEN(Token VARCHAR);");
        Cursor resultSet = db.rawQuery("Select * from REGIS_TOKEN",null);

        if(!(resultSet.moveToFirst()) || resultSet.getCount()==0)
        {
            rn = new Random();
            String new_token=rn.nextInt(10)+rn.nextInt(10)+rn.nextInt(10)+"FG";
            recTok=new_token;
            new Authenticate().execute(new_token);
            try {
                db.execSQL("INSERT INTO REGIS_TOKEN VALUES('"+new_token+"');");
            }catch(Exception e){Log.d("Networking","Unable to enter in local Database");}
        }
        else
        {
            resultSet.moveToFirst();
            String token=resultSet.getString(0);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView)findViewById(R.id.LV1);
        content.add("#SENDER-14FGFILLER");
        listAdapter = new ListAdapter(this,R.layout.smessage,content);
        listView.setAdapter(listAdapter);
        ed=(EditText)findViewById(R.id.ed1);
        bt=(Button)findViewById(R.id.bt1);

    }
    public void onStart()
    {

        super.onStart();
        Log.d("Networking","In On Start");
        new StartListener().execute(listAdapter);
    }


    public void startSocket(View view)
    {

        CharSequence str = "#SENDER-"+recTok+ed.getText();
        new ClientSocket().execute(str.toString());
        ed.setText("");



    }


}

