package com.example.button;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;


public class FragmentHome extends Fragment {


    ArrayList<HashMap<String,String>> contact_list;
    GPSTracker gps;

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    private Locator locator;
    double lat;
    double lng;
    long minTime;
    float minDistance;
    String locProvider;
    LocationManager locMgr;
    public  static final int RequestPermissionCode  = 1 ;
    String cuser;


    @TargetApi(Build.VERSION_CODES.M)


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final LocationManager manager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE );

        locator = Locator.getLocator(getActivity());
        mHelper = DbHelper.getInstance(getContext());

        SharedPreferences pref = getContext().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        cuser=pref.getString("username",null);


        ImageButton panic = (ImageButton) view.findViewById(R.id.btnSOS);

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(),

                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.SEND_SMS))
                    {
                      // Toast.makeText(getContext(),"Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.SEND_SMS},
                                RequestPermissionCode);

                    }

                } else {
                    contact_list = getAllContacts();
                    SmsManager smsMan = SmsManager.getDefault();

                    if (contact_list.size() != 0)
                    {
                        for(int i=0; i<contact_list.size(); i++)
                        {
                            HashMap<String, String> hashmapData = contact_list.get(i);
                            String name = hashmapData.get("name");
                            String phone = hashmapData.get("phone");
                            smsMan.sendTextMessage(phone,null,"Test Application",null,null);
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "Message sent to all contacts.",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "You don't have added any contacts yet.",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        return view;
    }


    public boolean internetConnetion()
    {

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else
            {
            return false;
        }
    }

    public void sendSMS(String name, String phone, String message)
    {
        String nm = name;
        String phoneNo = phone;
        String msg = message;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
           // Toast.makeText(getActivity().getApplicationContext(), "Message to "+nm+" Sent",
             //       Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Cannot send msg",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

        private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    public ArrayList<HashMap<String, String>> getAllContacts()
    {
        ArrayList<HashMap<String, String>> contact_list;
        contact_list = new ArrayList<HashMap<String, String>>();

        dataBase = mHelper.getWritableDatabase();
        Cursor cursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME +" WHERE " +DbHelper.KEY_CUSER+ " = + '"+cuser+"'", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("phone", cursor.getString(2));
                contact_list.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contact_list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(),"Permisssion Granted", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            break;
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}


