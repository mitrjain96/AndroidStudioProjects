package com.example.mitrjain.chatapp;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mit R Jain on 05-04-2017.
 */

public class sendMessage extends AsyncTask<String,Void,Long> {
    URL url;
    URLConnection urlConnection;
    HttpURLConnection conn;
    protected void onPreExecute()
    {
        try{
        url = new URL("https://fcm.googleapis.com/fcm/send");}
        catch(Exception e){Log.d("Networking",e.toString());}

    }
    @Override
    protected Long doInBackground(String... params)
    {
        try {
            conn = (HttpURLConnection) url.openConnection();
        }
        catch(Exception e) {
            Log.d("Networking url",e.toString());

        }
        try{

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAA_6CN7HU:APA91bEyjpAjftbbmFEWyx3rx4wcBowoHJLXLDKpTo4BIxWIt2NG8dN7ofpfV5tdkecDwZRkg-bLJR39ceWKZaEzuRQYeTNwUahY-pKjy_Vllu5AII6paWUsmgEtbTXdw0CnwO4bkXc2");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("to", params[2]);
            JSONObject data = new JSONObject();
            data.put("body",params[0]+"-"+params[1]+"-"+params[3]);
            json.put("data",data);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            Log.d("networking Response",conn.getResponseMessage());
        }
        catch(Exception e){
            Log.d("Networking",e.toString());}
        return new Long(5);
    }
}
