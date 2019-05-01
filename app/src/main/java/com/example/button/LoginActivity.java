package com.example.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends Activity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    ArrayList<HashMap<String,String>> user_list;
    String user,pass;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mHelper = DbHelper.getInstance(this);


        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);
        final EditText etUsername = (EditText) findViewById(R.id.username);
        final EditText etPassword = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Username and Password are empty",Toast.LENGTH_LONG).show();
                }
                else
                    {
                        user_list=getAllUsers();
                        user=etUsername.getText().toString().trim();
                        pass=etPassword.getText().toString().trim();

                        for(int i=0; i<user_list.size(); i++)
                        {
                            HashMap<String, String> hashmapData = user_list.get(i);
                            String user1 = hashmapData.get("user");
                            String pass1 = hashmapData.get("pass");

                         if(user.equals(user1) && pass.equals(pass1))
                         {
                             flag=true;
                             SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                             SharedPreferences.Editor edt = pref.edit();
                             edt.putBoolean("activity_executed", true);
                             edt.putString("username",user);
                             edt.commit();

                             String s=pref.getString("username",null);

                             Intent is = new Intent(LoginActivity.this, MainActivity.class);
                             startActivity(is);
                             finish();
                             overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                             Toast.makeText(getApplicationContext(),"" +s+ " Logged in Successfully",Toast.LENGTH_LONG).show();
                         }
                        }
                        if(flag==false)
                        {
                            Toast.makeText(getApplicationContext(),"Username and Password doesn't match",Toast.LENGTH_LONG).show();
                        }
                }
            }
        });

        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
        });
    }

    public ArrayList<HashMap<String, String>> getAllUsers()
    {
        ArrayList<HashMap<String, String>> user_list;
        user_list = new ArrayList<HashMap<String, String>>();

        dataBase = mHelper.getWritableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME2, null);

        if (cursor.moveToFirst())
        {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("user", cursor.getString(1));
                map.put("pass", cursor.getString(2));
                user_list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return user_list;
    }
}


