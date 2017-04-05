package com.example.mitrjain.chatapp;

/**
 * Created by Mit R Jain on 05-04-2017.
 */

public class userDetails {
        String name;
        String contact_no;
        String passwd;
        String email;
        String refreshedToken;
    userDetails(String name,String contact, String passwd, String email, String refreshedToken)
    {
        this.name=name;
        this.contact_no=contact;
        this.email=email;
        this.passwd=passwd;
        this.refreshedToken=refreshedToken;
    }


}
