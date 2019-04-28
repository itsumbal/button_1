package com.example.button;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper mInstance = null;
    Context context;
    static String DATABASE_NAME="userdata";
    public static final String TABLE_NAME="contacts";
    public static final String TABLE_NAME2="user";

    public static final String KEY_NAME="name";
    public static final String KEY_PHONE="phone";
    public static final String KEY_CUSER="cuser";

    public static final String KEY_USER="user_name";
    public static final String KEY_PASS="user_pass";

    public static final String KEY_ID="id";
    public static final String KEY_ID2="id2";
    public String cuser;

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    public static DbHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+KEY_NAME+" TEXT, "+KEY_PHONE+" TEXT, "+KEY_CUSER+" TEXT)";

        String CREATE_TABLE2="CREATE TABLE "+TABLE_NAME2+" ("+KEY_ID2+" INTEGER PRIMARY KEY, "+KEY_USER+" TEXT, "+KEY_PASS+" TEXT)";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long getUserCount() {

        SharedPreferences pref = context.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        String s=pref.getString("username",null);
        cuser=s;

        mHelper = DbHelper.getInstance(context);

        long count=0;

        dataBase = mHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME +" WHERE " +DbHelper.KEY_CUSER+ " = + '"+cuser+"'", null);

        if (mCursor.moveToFirst()) {
            do {
                count++;

            } while (mCursor.moveToNext());
        }

        return count;
    }

}