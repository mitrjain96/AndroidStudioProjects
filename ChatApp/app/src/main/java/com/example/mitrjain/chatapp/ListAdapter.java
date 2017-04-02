package com.example.mitrjain.chatapp;

import android.content.ClipData;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mit R Jain on 29-03-2017.
 */

public class ListAdapter extends ArrayAdapter<String> {
    Color c = new Color();

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.smessage, null);
        }

        String p = getItem(position);

        if (p != null) {
            TextView tv = (TextView) v.findViewById(R.id.textView);


            if(p.substring(0,4).equals("#REC"))
            {
                tv.setGravity(Gravity.RIGHT);
                tv.setBackgroundColor(Color.parseColor("#E3F2FD"));
                p=p.substring(4);

            }
            else
            {
                tv.setGravity(Gravity.LEFT);
                tv.setBackgroundColor(Color.parseColor("#64FFDA"));
                p=p.substring(12);
            }

            tv.setText(p);

        }

        return v;
    }

}