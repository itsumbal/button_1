package com.example.button;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> id;
    private ArrayList<String> name;
    private ArrayList<String> phone;


    public DisplayAdapter(Context c, ArrayList<String> id, ArrayList<String> name, ArrayList<String> phone) {
        this.mContext = c;

        this.id = id;
        this.name =name;
        this.phone = phone;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.contactlist, null);
            mHolder = new Holder();
            mHolder.txt_id = (TextView) child.findViewById(R.id.ltxtid);
            mHolder.txt_name = (TextView) child.findViewById(R.id.ltxtName);
            mHolder.txt_phone = (TextView) child.findViewById(R.id.ltxtPhone);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        mHolder.txt_id.setText(id.get(pos));
        mHolder.txt_name.setText(name.get(pos));
        mHolder.txt_phone.setText(phone.get(pos));

        return child;
    }

    public class Holder {
        TextView txt_id;
        TextView txt_name;
        TextView txt_phone;
    }

}