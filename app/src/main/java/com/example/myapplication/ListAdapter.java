package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context mContext;

    public ListAdapter(Context context, int resource, String[] items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        String p = getItem(position);
        Log.i("string", p);
        TextView tt1 = (TextView) v.findViewById(R.id.lyric);
        tt1.setText(p);
        return v;
    }

}