package com.example.button;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends Activity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    private String id, user,pass,pass2;
    ArrayList<HashMap<String,String>> user_list;
boolean flag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mHelper = DbHelper.getInstance(this);

        final Button signup = (Button) findViewById(R.id.signup1);
        final EditText username = (EditText) findViewById(R.id.susername);
        final EditText password = (EditText) findViewById(R.id.spassword);
        final EditText cpassword = (EditText) findViewById(R.id.cpassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag=false;
                user=username.getText().toString().trim();
                pass=password.getText().toString().trim();
                pass2=cpassword.getText().toString().trim();


                if(user.isEmpty() || pass.isEmpty() || pass2.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Username and Password are empty",Toast.LENGTH_LONG).show();
                }

                else if (pass.equals(pass2))
                {
                        user_list=getAllUsers();
                    for(int i=0; i<user_list.size(); i++)
                    {
                        HashMap<String, String> hashmapData = user_list.get(i);
                        String user1 = hashmapData.get("user");

                        if(user.equals(user1))
                        {
                            flag=true;
                            Toast.makeText(getApplicationContext(),"Username Already exists",Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    if(flag==false)
                    {
                        saveData();
                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getApplicationContext(),"Signed up Successfully",Toast.LENGTH_LONG).show();
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void saveData()
    {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_USER,user);
        values.put(DbHelper.KEY_PASS,pass );


        dataBase.insert(DbHelper.TABLE_NAME2, null, values);

        dataBase.close();
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

