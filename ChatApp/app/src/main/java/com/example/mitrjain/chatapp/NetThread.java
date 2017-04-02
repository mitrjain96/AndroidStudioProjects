package com.example.mitrjain.chatapp;

import android.util.Log;
import android.widget.ArrayAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import static android.R.id.message;

public class NetThread extends Thread {
    SocketAddress sockaddr;
    Socket s;
    BufferedReader br;
    ListAdapter listadapter;
    NetThread( ListAdapter Listadapter)
    {
        this.listadapter=Listadapter;
        this.start();
    }
    public void run()
    {
        Log.d("Networking in NetThread","Starting to accept Sockets");
        InetAddress addr;
        try{
            addr = InetAddress.getByName("localhost");
            int port = 8889;
            sockaddr = new InetSocketAddress(addr, port);
            ServerSocket lsocket = new ServerSocket(port);
            s=lsocket.accept();
            Log.d("Networking in NetThread",addr.toString()+"--"+sockaddr.toString());
        }catch(Exception e){Log.d("Networking",e.toString());}


        try{
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }catch(Exception e){
            Log.d("Networking",e.toString());}
        String message="";
          try{
                message=br.readLine();
            }catch(Exception e){
                Log.d("Networking",e.toString());}
            listadapter.add(("#REC"+message).toString());
            listadapter.notifyDataSetChanged();

    }
}
