package com.example.button;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<MyListData> listdata;
    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    String name,phone;
    private AlertDialog.Builder build;
    private ArrayList<String> userId = new ArrayList<String>();
    private ArrayList<String> user_Name = new ArrayList<String>();
    private ArrayList<String> user_Phone = new ArrayList<String>();

    private ListView userList;
    DisplayAdapter disadpt;


    // RecyclerView recyclerView;
    public MyListAdapter(List<MyListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata.get(position);
        holder.textView1.setText(listdata.get(position).getname());
        holder.textView.setText(listdata.get(position).getphone());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mHelper = DbHelper.getInstance(view.getRootView().getContext());

             name=myListData.getname().toString().trim();
             phone=myListData.getphone().toString().trim();
             if(mHelper.getUserCount()<13)
             {
                saveData();
                 Toast.makeText(view.getContext(),"" +myListData.getname()+ " added",Toast.LENGTH_LONG).show();
             }
             else
             {
                 AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                 alertBuilder.setTitle("Limit Exceed");
                 alertBuilder.setMessage("Cannot add more contacts");
                 alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                     public void onClick(DialogInterface dialog, int which) {
                         dialog.cancel();

                     }
                 });
                 alertBuilder.create().show();
             }
             //   Toast.makeText(view.getContext(),"click on item: "+myListData.getname(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
       return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView1 = (TextView) itemView.findViewById(R.id.textview1);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    void saveData()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_NAME,name);
        values.put(DbHelper.KEY_PHONE,phone );

        dataBase.insert(DbHelper.TABLE_NAME, null, values);
        dataBase.close();
    }
}