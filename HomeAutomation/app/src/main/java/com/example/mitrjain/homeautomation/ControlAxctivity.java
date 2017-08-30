package com.example.mitrjain.homeautomation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

/**
 * Created by Mit R Jain on 17-08-2017.
 */

public class ControlAxctivity extends AppCompatActivity {
   Switch master,fans,lights,tv;
    Integer switchCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_control);
        master = (Switch)findViewById(R.id.master_switch);
        fans = (Switch)findViewById(R.id.fans_switch);
        lights = (Switch)findViewById(R.id.lights_switch);
        tv = (Switch)findViewById(R.id.tv_switch);




    }

    public void switchTrigger(View v)
    {
        Switch s = (Switch)v;
        if(s.getId() == R.id.master_switch)
        {
            if(master.isChecked())
            {
                fans.setChecked(true);
                lights.setChecked(true);
                tv.setChecked(true);
                switchCount=3;
            }
            else{
                fans.setChecked(false);
                lights.setChecked(false);
                tv.setChecked(false);
                switchCount=0;
            }
        }
        else {
            if(s.isChecked())
                switchCount++;
            else
                switchCount--;
            if(switchCount==3) {
                master.setChecked(true);
                //switchTrigger(master);
            }
            else
            {
                master.setChecked(false);
            }
        }
        Log.d("CaptiosX",switchCount.toString());

    }

}
