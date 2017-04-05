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
    String message="";
    NetThread( String message )
    {
        this.message=message;
        this.start();
    }
    public void run()
    {
        new MainActivity.updateListView().execute(message);

    }
}
