package com.example.mitrjain.chatapp;

import android.util.Log;

/**
 * Created by Mit R Jain on 05-04-2017.
 */

public class userDetails {
        String name;
        String contact_no;
        String passwd;
        String email;
        String refreshedToken;
    userDetails(String name, String contact_no ,String passwd,String email,String refreshedToken ){
        this.name=name;
        this.contact_no=contact_no;
        this.passwd=passwd;
        this.email=email;
        this.refreshedToken=refreshedToken;

    }

    userDetails(String str)
    {

        Log.d("NetworkingUserDetails",str);
        String details[] = str.split("\\s+");

        this.contact_no=details[0];
        this.email=details[1];
        this.name=details[2];
        this.passwd=details[3];
        this.refreshedToken=details[4];
    }


}
