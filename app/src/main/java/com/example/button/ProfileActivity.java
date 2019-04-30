package com.example.button;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;

    private String cuser;
    private  String fullname;
    private String genderr;
    private  String password;

    public AlertDialog dialog;
    public AlertDialog dialog1;
    public AlertDialog dialog2;

    public EditText editText;
    public EditText editText1;
    public EditText editText2;

    public TextView name;
    public  TextView gender;
    public  TextView pass;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);

        mHelper = DbHelper.getInstance(this);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        String s=pref.getString("username",null);
        cuser=s;

        showProfileData();

        TextView uname = (TextView) findViewById(R.id.pnamee);
        uname.setText("Welcome " +cuser);

        TextView username = (TextView) findViewById(R.id.puser1);
        username.setText(cuser);

         name = (TextView) findViewById(R.id.pname1);
         pass = (TextView) findViewById(R.id.ppass1);
         gender = (TextView) findViewById(R.id.pgender1);


         // ------------- EDIT NAME

        editText = new EditText(this);
        dialog = new AlertDialog.Builder(this).create();

        dialog.setTitle("Edit the Name");
        dialog.setView(editText);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "save name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sa;
                sa=editText.getText().toString();

                if(sa.isEmpty())
                Toast.makeText(getApplicationContext(),"name cannot be empty",Toast.LENGTH_LONG).show();

                else if(sa.length()<4 || sa.length()>24)
                    Toast.makeText(getApplicationContext(),"name is too long or short",Toast.LENGTH_LONG).show();

                else {
                    fullname = editText.getText().toString().trim();
                    saveName();
                    //showProfileData();
                    name.setText(editText.getText().toString());
                    }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(name.getText());
              dialog.show();
            }
        });


    // ------ EDIT PASSWORD

        editText1 = new EditText(this);
        dialog1 = new AlertDialog.Builder(this).create();

        dialog1.setTitle("Edit the Password");
        dialog1.setView(editText1);

        dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "save password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sa;
                sa=editText1.getText().toString();

                if(sa.isEmpty())
                Toast.makeText(getApplicationContext(),"password cannot be empty",Toast.LENGTH_LONG).show();

                else if(sa.length()<5 || sa.length()>10)
                    Toast.makeText(getApplicationContext(),"password should be at least of 5 characters and not longer than 10 characters",Toast.LENGTH_LONG).show();

                else {
                    password = editText.getText().toString().trim();
                    savePassword();
                    //showProfileData();
                    pass.setText(editText1.getText().toString());
                }
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText(pass.getText());
                dialog1.show();
            }
        });

        // ------ EDIT GENDER

        editText2 = new EditText(this);
        dialog2 = new AlertDialog.Builder(this).create();

        dialog2.setTitle("Edit the Gender");
        dialog2.setView(editText2);

        dialog2.setButton(DialogInterface.BUTTON_POSITIVE, "save gender", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sa;
                sa = editText2.getText().toString();

                if (sa.isEmpty())
                    Toast.makeText(getApplicationContext(), "gender cannot be empty", Toast.LENGTH_LONG).show();

                else if (!(sa.equalsIgnoreCase("female") || sa.equalsIgnoreCase("male")))
                    Toast.makeText(getApplicationContext(), "incorrect gender", Toast.LENGTH_LONG).show();

                else {
                    genderr = editText.getText().toString().trim();
                    saveGender();
                    //showProfileData();
                    gender.setText(editText2.getText().toString());
                }
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2.setText(gender.getText());
                dialog2.show();
            }
        });

    }

    void saveName()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_FULLNAME,fullname);

        dataBase.insert(DbHelper.TABLE_NAME2, null, values);

        dataBase.close();
    }

    void savePassword()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_PASS,password);

        dataBase.insert(DbHelper.TABLE_NAME2, null, values);

        // ERROR WHILE UPDATING DATABASE IN EVERY SAVE FUNCTION

      //  dataBase.update(DbHelper.TABLE_NAME2, values, DbHelper.KEY_USER + " = " + "'" + cuser + "'", null);

        dataBase.close();
    }

    void saveGender()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_GENDER,genderr);

        dataBase.insert(DbHelper.TABLE_NAME2, null, values);

        dataBase.close();
    }

    public void showProfileData() {
        name = (TextView) findViewById(R.id.pname1);
        pass = (TextView) findViewById(R.id.ppass1);
        gender = (TextView) findViewById(R.id.pgender1);

        String password="";
        String fullname="";
        String gender_="";

        dataBase = mHelper.getReadableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT " +DbHelper.KEY_PASS+ ", " +DbHelper.KEY_FULLNAME+ ", " +DbHelper.KEY_GENDER+ " FROM " + DbHelper.TABLE_NAME2 + " WHERE " + DbHelper.KEY_USER + " = " + "'" + cuser + "'", null);

        Log.i("cursor2","i am here");

        if(cursor!=null) {
            if (cursor.moveToNext()) {
                Log.i("cursor", "chal gaya ");
                password = cursor.getString(0);
                fullname = cursor.getString(1);
                gender_ = cursor.getString(2);
            }
        }

        pass.setText(password);
            name.setText(fullname);
            gender.setText(gender_);

            if(name.getText().toString().isEmpty())
                name.setText("enter name");

            if(gender.getText().toString().isEmpty())
                gender.setText("enter gender");

        cursor.close();

    }

    public void onBackPressed() {
        finish();
    }

}
