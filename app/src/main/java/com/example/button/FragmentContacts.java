package com.example.button;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentContacts extends Fragment {


    private Button btn_save;
    private EditText edit_name, edit_phone;
    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    private String id, name, phone;
    private boolean isUpdate;

    private ArrayList<String> userId = new ArrayList<String>();
    private ArrayList<String> user_Name = new ArrayList<String>();
    private ArrayList<String> user_Phone = new ArrayList<String>();

    private ListView userList;
    DisplayAdapter disadpt;
    private AlertDialog.Builder build;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactlist, container, false);


        userList = (ListView) view.findViewById(R.id.lvWhitelist);
        Button btnAdd = (Button) view.findViewById(R.id.btnAddContact);

        mHelper = DbHelper.getInstance(getContext());


        displayData();

        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                build = new AlertDialog.Builder(getActivity());
                build.setTitle("Delete " + user_Name.get(arg2));
                build.setMessage("Do you want to delete ?");
                build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(),
                                user_Name.get(arg2)
                                        + " is deleted.", Toast.LENGTH_SHORT).show();

                        dataBase.delete(
                                DbHelper.TABLE_NAME,
                                DbHelper.KEY_ID + "="
                                        + userId.get(arg2), null);
                        displayData();
                        dialog.cancel();
                    }
                });

                build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = build.create();
                alert.show();

                return true;
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show(1);
            }
        });
        return view;
    }

    // ----- selecting from where to add contacts


    public void show(int dialogid) {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Add Contact");
        d.setContentView(R.layout.choose_contact);
        d.show();

        Button fromphone = (Button) d.findViewById(R.id.fromphone);
        Button manually = (Button) d.findViewById(R.id.manually);

        manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                show2(2);
            }

        });

        fromphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();

                Intent intent = new Intent(getActivity(), RecyclerActivity.class);
                startActivity(intent);

            }

        });
    }

    // ---- Adding Contacts Manually

    public void show2(int dialogid) {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Add Contact");
        d.setContentView(R.layout.addcontact);
        Button btncancel = (Button) d.findViewById(R.id.cancel);
        Button btnsend = (Button) d.findViewById(R.id.add);

        if (dialogid == 2) {
            btnsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mHelper.getUserCount() == 13) {

                       d.dismiss();
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setTitle("Limit Exceed");
                        alertBuilder.setMessage("Cannot add more contacts");
                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        alertBuilder.create().show();
                    displayData();
                    }

                    else
                    {

                    edit_name = (EditText) d.findViewById(R.id.txtName);
                    edit_phone = (EditText) d.findViewById(R.id.txtPhone);

                    name = edit_name.getText().toString().trim();
                    phone = edit_phone.getText().toString().trim();

                    if (name.length() > 0 && phone.length() > 0) {
                        saveData();
                        d.dismiss();
                        displayData();
                    } else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setTitle("Invalid Data");
                        alertBuilder.setMessage("Please, Enter valid data");
                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        alertBuilder.create().show();
                    }
                }
                }

            });
        }
        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }



    void saveData()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_NAME,name);
        values.put(DbHelper.KEY_PHONE,phone );

      //  System.out.println("");
      //  if(isUpdate)
      //  {
            //update database with new data
       //     dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID+"="+id, null);
       // }
       // else
       // {
            //insert data into database
            dataBase.insert(DbHelper.TABLE_NAME, null, values);
       // }
        //close database
        dataBase.close();
    }


    private void displayData()
    {
        dataBase = mHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);

        userId.clear();
        user_Name.clear();
        user_Phone.clear();
        if (mCursor.moveToFirst()) {
            do {
                userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
                user_Name.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_NAME)));
                user_Phone.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_PHONE)));

            } while (mCursor.moveToNext());
        }
        disadpt = new DisplayAdapter(getActivity(),userId, user_Name, user_Phone);
        userList.setAdapter(disadpt);
        mCursor.close();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // setUserVisibleHint(true);
    }

}


