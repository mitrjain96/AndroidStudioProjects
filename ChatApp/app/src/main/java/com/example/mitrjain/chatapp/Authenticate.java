package com.example.mitrjain.chatapp;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by ramesh on 3/22/2017.
 */

class Authenticate extends AsyncTask<String,Void,Long> {

    Integer size=1024;
    Socket s;
    protected void onPreExecute()
    {

    }
    @Override
    protected Long doInBackground(String... params)
    {
        try{
            s=new Socket("192.168.137.1",8888);

        }
        catch(Exception e){
            Log.d("Networking",e.toString());}
        params[0]="#REGTOK-"+params[0];
        size  =params[0].length();
        Log.d("Networking",params[0]+" Size=" +size.toString());
        try {
            if(s!=null) {
                DataOutputStream wr = new DataOutputStream(s.getOutputStream());
                wr.writeBytes(size.toString());
                wr.writeBytes("!");
                wr.write(params[0].getBytes());

                wr.close();
            }
            else
            {
                Log.d("Networking","Empty Socket");
            }
        }
        catch(Exception e){Log.d("Networking",e.toString());}
        try{s.close();}
        catch(Exception e){Log.d("Networking",e.toString());}
        return (long)5;
    }
    protected void onPostExecute(Long s) {

    }

}
