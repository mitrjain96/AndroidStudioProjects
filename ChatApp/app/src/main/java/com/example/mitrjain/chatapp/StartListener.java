package com.example.mitrjain.chatapp;

import android.os.AsyncTask;

/**
 * Created by Mit R Jain on 01-04-2017.
 */

public class StartListener extends AsyncTask<ListAdapter,Void,Long> {
    protected void onPreExecute()
    {}
    @Override
    protected Long doInBackground(ListAdapter... params)
    {
        NetThread reciever = new NetThread(params[0]);
        return Long.valueOf(5);
    }
    protected void onPostExeute(Long l)
    {}


}
