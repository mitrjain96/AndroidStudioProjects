package com.example.mitrjain.chatapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Mit on 23-03-2017.
 */

class CommunicationThreadTask extends AsyncTask<ArrayAdapter<String>,Void,Thread>  {
    Socket s;
    @Override
    protected Thread doInBackground(ArrayAdapter<String>... params)
    {
        try{
            s=new Socket("192.168.137.1",8888);

        }
        catch(Exception e){
            Log.d("Networking",e.toString());}
        NetThread t=new NetThread(s,params[0]);



        return t;
    }
    protected void onPostExecute(Thread t) {

    }

}
