package com.example.mitrjain.chatapp;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class messageReceivingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        String msg="";
        Log.d("Network here","CALLED");
        Log.d("Networking", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("Networking", "Message data payload: " + remoteMessage.getData());
            msg=remoteMessage.getData().get("body");
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("Networking", "Message Notification Body: " + remoteMessage.getNotification());
            msg=remoteMessage.getNotification().getBody();
        }

        if(msg!=null)
        {
            new NetThread(msg);
        }
    }

}
