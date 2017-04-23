package com.example.kata.onlab.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by android on 2016. 10. 18..
 */
public class LDLocationManager {

    private Context context;
    private LocationListener listener;
    private LocationManager locMan;

    public LDLocationManager(Context aContext, LocationListener listener) {
        context = aContext;
        this.listener = listener;
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationMonitoring() {


        locMan.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, 100, listener);
        // EMULÁTORON A NETWORK PROVIDER NEM ÉRHETŐ EL!!!
        /*locMan.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0, 0, listener);*/
    }

    public void stopLocationMonitoring() {
        if (locMan != null) {
            locMan.removeUpdates(listener);
        }
    }
}