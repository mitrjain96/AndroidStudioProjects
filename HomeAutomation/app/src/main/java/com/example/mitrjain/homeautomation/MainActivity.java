package com.example.mitrjain.homeautomation;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> listOfProfiles = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton img1 = (ImageButton)findViewById(R.id.Img1);
        ImageButton img2 = (ImageButton)findViewById(R.id.Img2);
        ImageButton img3 = (ImageButton)findViewById(R.id.Img3);
        ListView profiles = (ListView) findViewById(R.id.Profiles);
        DrawerLayout navDrawer = (DrawerLayout)findViewById(R.id.navDrawer);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        for(Integer i=1;i<=10;i++)
        {
            listOfProfiles.add("Profile "+i.toString());
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        navDrawer.addDrawerListener(toggle);
        ArrayAdapter listAdapter=new ArrayAdapter(this,R.layout.navigationfield,listOfProfiles);
        profiles.setAdapter(listAdapter);
    }
    public  void navigate(View v) {
        int vId = v.getId();

        switch (vId) {
            case R.id.Img1:
                Intent i = new Intent(MainActivity.this, ControlAxctivity.class);
                startActivity(i);
                break;
//            case R.id.Img2:
//                break;
//            case R.id.Img3:
//                break;
            default:
                Toast.makeText(this, "ERROR WHILE NAVIGATING", Toast.LENGTH_SHORT).show();
        }
    }

}
