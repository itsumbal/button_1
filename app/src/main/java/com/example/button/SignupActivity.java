package com.example.button;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        final Button signup = (Button) findViewById(R.id.signup1);
        final EditText username = (EditText) findViewById(R.id.susername);
        final EditText password = (EditText) findViewById(R.id.spassword);
        final EditText cpassword = (EditText) findViewById(R.id.cpassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || cpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Username and Password are empty",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }
}

