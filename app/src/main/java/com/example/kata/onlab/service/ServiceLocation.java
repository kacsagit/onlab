package com.example.kata.onlab.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.kata.onlab.R;
import com.example.kata.onlab.manager.MyLocationManager;
import com.example.kata.onlab.ui.main.MainActivity;



public class ServiceLocation extends Service implements LocationListener,MyLocationManager.OnLocChanged {
    public static final String BR_NEW_LOCATION = "BR_NEW_LOCATION";
    public static final String KEY_LOCATION = "KEY_LOCATION";


    private MyLocationManager ldLocationManager = null;
    private boolean locationMonitorRunning = false;

    private Location firstLocation = null;
    private Location lastLocation = null;

    private final int NOTIF_FOREGROUND_ID = 101;

    private IBinder binderServiceLocation = new BinderServiceLocation();

    @Override
    public IBinder onBind(Intent intent) {
        return binderServiceLocation;
    }

    public boolean isLocationMonitorRunning() {
        return locationMonitorRunning;
    }

    public Location getLastLocation() {
        return lastLocation;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIF_FOREGROUND_ID, getMyNotification("starting..."));
        firstLocation = null;



        if (!locationMonitorRunning) {
            locationMonitorRunning = true;
            ldLocationManager = new MyLocationManager(this, this);
            ldLocationManager.startLocatoinMonitoring();
        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ldLocationManager != null) {
            ldLocationManager.stopLocationMonitoring();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateNotification("Lat: " + location.getLatitude() + "n" +
                "Lng: " + location.getLongitude());


        if (firstLocation == null) {
            firstLocation = location;
        }
        lastLocation = location;

        Intent intent = new Intent(BR_NEW_LOCATION);
        intent.putExtra(KEY_LOCATION, location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        updateNotification("Status changed: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        updateNotification("Provider enabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        updateNotification("Provider disabled: " + provider);
    }


    private Notification getMyNotification(String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                NOTIF_FOREGROUND_ID,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Location monitoring")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{1000, 2000, 1000})
                .setContentIntent(contentIntent).build();
        return notification;
    }

    // Régebbi API szintek esetén a kiemelt sorban használjuk a getNotification() metódust a build() helyett.

    private void updateNotification(String text) {
        Notification notification = getMyNotification(text);
        NotificationManager notifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifMan.notify(NOTIF_FOREGROUND_ID, notification);
    }

    @Override
    public void locationChanged(Location location) {

    }


    public class BinderServiceLocation extends Binder {
        public ServiceLocation getSerivce() {
            return ServiceLocation.this;
        }
    }
}


