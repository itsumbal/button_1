package com.example.button;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper mInstance = null;
    Context context;
    static String DATABASE_NAME="userdata";

    public static final String TABLE_NAME="contacts";
    public static final String TABLE_NAME2="user";
    public static final String TABLE_NAME3="accidents";


    public static final String KEY_NAME="name";
    public static final String KEY_PHONE="phone";
    public static final String KEY_CUSER="cuser";

    public static final String KEY_USER="user_name";
    public static final String KEY_PASS="user_pass";
    public static final String KEY_FULLNAME="full_name";
    public static final String KEY_GENDER="user_gender";

    public static final String KEY_USERNAME="user_name";
    public static final String KEY_LOCATION="acc_location";
    public static final String KEY_TIME="acc_time";


    public static final String KEY_ID="id";
    public static final String KEY_ID2="id2";
    public static final String KEY_ID3="id3";

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

        String CREATE_TABLE2="CREATE TABLE "+TABLE_NAME2+" ("+KEY_ID2+" INTEGER PRIMARY KEY, "+KEY_USER+" TEXT, "+KEY_PASS+" TEXT, "+KEY_FULLNAME+" TEXT,"+KEY_GENDER+" TEXT)";

        String CREATE_TABLE3="CREATE TABLE "+TABLE_NAME3+" ("+KEY_ID3+" INTEGER PRIMARY KEY, "+KEY_USERNAME+" TEXT, "+KEY_LOCATION+" TEXT, "+KEY_TIME+" TEXT);";


        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);

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

    //////////////////////////to see database
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}