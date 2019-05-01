package com.example.button;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;


public class Locator extends Service implements android.location.LocationListener {

    private Context context;
    private final Activity a;
    private LocationManager lm;
    private Location loc = null;
    double lat;
    double lng;
    private boolean gpsEnabled = false, networkEnabled = false;
    public  static final int RequestPermissionCode  = 1 ;

    private static Locator ref;

    @TargetApi(Build.VERSION_CODES.M)

    private Locator(Context context, Activity a) {
        this.context = context;
        this.a=a;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        requestUpdate(context,a);
    }

    public void requestUpdate(Context c, Activity a) {
        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Toast.makeText(getContext(),"Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RequestPermissionCode);
            }

        } else {

            if (gpsEnabled)
                lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
            else if (networkEnabled)
                lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }
    }

    public Location getLocation(Context c, Activity a) throws NoPositionProvidersException {

        String provider = null;
        Location tmpLoc = null;
        Location ret = new Location("");

        if (ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);

            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // permission granted

            } else {
                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RequestPermissionCode);
            }

        }

        else {

            if (gpsEnabled) {
                provider = LocationManager.GPS_PROVIDER;
                tmpLoc = lm.getLastKnownLocation(provider);
            }

            if (networkEnabled && tmpLoc == null) {
                provider = LocationManager.NETWORK_PROVIDER;
                tmpLoc = lm.getLastKnownLocation(provider);
            }

            if (provider == null) {
                throw new NoPositionProvidersException("Can't find position provider");
            }

            if (tmpLoc != null) {
                if (loc == null || tmpLoc.getAccuracy() > loc.getAccuracy())
                    loc = tmpLoc;
            }

            if (loc == null) {
                throw new NoPositionProvidersException("Can't find position provider");
            }
        //    Location ret = new Location("");
            ret.setLatitude(loc.getLatitude());
            ret.setLongitude(loc.getLongitude());
            setLattitude(loc.getLatitude());
            setLongitude(loc.getLongitude());
        }
        return ret;
    }

    public void setLattitude(double lat)
    {
        this.lat=lat;
    }

    public void setLongitude(double lng)
    {
        this.lng=lng;
    }

    public double getLat()
    {
    return lat;
    }

    public double getLng() {
        return lng;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Location services disabled");

        // Setting Dialog Message
        alertDialog.setMessage("Please enable them in the settings");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static String getAddressFromLocation(Location loc, Context context){
        Geocoder g = new Geocoder(context, Locale.getDefault());
        Address a = null;

        int tries = 5;

        for (int i = 0; i < tries; i++) {
            try {
                a = g.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1).get(0);
                if (a == null) g.getFromLocation(loc.getLatitude(), loc.getLongitude(), i+1).get(i);
            } catch (IOException | IndexOutOfBoundsException ignored) {
            }
        }

        if (a == null) return "";

        String ret = a.getAddressLine(0) ;
        return ret;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Permisssion Granted", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class NoPositionProvidersException extends Throwable {
        private NoPositionProvidersException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static Locator getLocator(Context context, Activity a) {
        if (ref == null) ref = new Locator(context,a);
        return ref;
    }
}
