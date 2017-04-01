package com.example.mitrjain.chatapp;

import android.util.Log;
import android.widget.ArrayAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import static android.R.id.message;

public class NetThread extends Thread {
    Socket s;
    BufferedReader br;
    ArrayAdapter<String> listadapter;
    NetThread(Socket s, ArrayAdapter<String> Listadapter)
    {
        this.s=s;
        this.listadapter=Listadapter;

        this.start();
    }
    public void run()
    {
        try{
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }catch(Exception e){
            Log.d("Networking",e.toString());}
        String message="";
        while(!message.equals("Server Collapse"))
        {
            try{
                message=br.readLine();
            }catch(Exception e){
                Log.d("Networking",e.toString());}
            listadapter.add(message.toString());
            listadapter.notifyDataSetChanged();
        }
    }
}
