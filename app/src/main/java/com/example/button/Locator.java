package com.example.button;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.io.IOException;
import java.util.Locale;


public class Locator implements android.location.LocationListener {

    private Context context;
    private LocationManager lm;
    private Location loc = null;
    private boolean gpsEnabled = false, networkEnabled = false;


    private static Locator ref;

    private Locator(Context context) {
        this.context = context;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      //  requestUpdate();
    }


    public Location getLocation(Context c, Activity a) throws NoPositionProvidersException {

        gpsEnabled=true;

        networkEnabled=true;

        String provider = null;
        Location tmpLoc = null;

        if (ContextCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(a, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }


        if (gpsEnabled) {
            provider = LocationManager.GPS_PROVIDER;
            tmpLoc = lm.getLastKnownLocation(provider);
        }

        if (networkEnabled && tmpLoc == null) {
            provider = LocationManager.NETWORK_PROVIDER;
            tmpLoc = lm.getLastKnownLocation(provider);
        }

        if (provider == null){
            throw new NoPositionProvidersException("Can't find position provider");
        }

        if (tmpLoc != null) {
            if (loc == null || tmpLoc.getAccuracy() > loc.getAccuracy())
                loc = tmpLoc;
        }

        if (loc == null){
            throw new NoPositionProvidersException("Can't find position provider");
        }

        Location ret = new Location("");
        ret.setLatitude(loc.getLatitude());
        ret.setLongitude(loc.getLongitude());
        return ret;
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

        if (a == null) return null;

        String ret = a.getAddressLine(0) + "\n" + a.getAddressLine(1);
        return ret;
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

    public class NoPositionProvidersException extends Throwable {
        private NoPositionProvidersException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static Locator getLocator(Context context) {
        if (ref == null) ref = new Locator(context);
        return ref;
    }
}
