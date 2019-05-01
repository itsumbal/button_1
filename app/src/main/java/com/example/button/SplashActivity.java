package com.example.button;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class SplashActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGHT = 2000;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);
        LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll);
        ll2.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.splash2));
        new Handler().postDelayed(new Runnable() {
            public void run() {


                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

                if (pref.getBoolean("activity_executed", false)) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
