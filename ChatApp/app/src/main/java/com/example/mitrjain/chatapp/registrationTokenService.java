package com.example.mitrjain.chatapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Mit R Jain on 05-04-2017.
 */

public class registrationTokenService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh()
    {
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        Log.d("Networking",refreshedToken);
        if(!refreshedToken.equals(""))
            firstTimeRegister.setRefreshedToken(refreshedToken);
    }

}
