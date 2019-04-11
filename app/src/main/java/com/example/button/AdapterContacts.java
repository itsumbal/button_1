package com.example.button;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterContacts extends BaseAdapter {
    private final Context mContext;
    private final List<ItemContacts> mListInfo;

    public AdapterContacts(Context context, List<ItemContacts> list) {
        mContext = context;
        mListInfo = list;
    }

    @Override
    public int getCount() {
        return mListInfo.size();
    }

    @Override
    public Object getItem(int pos) {
        return mListInfo.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // inflating list view layout if null
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contactlist, parent, false);
        }

        // set nama
        TextView tvName = (TextView) convertView.findViewById(R.id.txtNama);
        tvName.setText(mListInfo.get(pos).getNama());

        TextView tvPhone = (TextView) convertView.findViewById(R.id.txtNoTelp);
        tvPhone.setText(mListInfo.get(pos).getNotelp());

        return convertView;
    }
}
