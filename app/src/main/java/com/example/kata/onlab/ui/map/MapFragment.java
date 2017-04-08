package com.example.kata.onlab.ui.map;


import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kata.onlab.R;
import com.example.kata.onlab.network.Data;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.ui.AddPlaceFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, MyLocationManager.OnLocChanged, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MapScreen {

    private static final String TAG = "MapFragment";
    private MyLocationManager myLocationManager = null;

    private Location prevLoc = null;
    private MapView mapView;
    private static GoogleMap googleMap;

    private List<Geofence> mGeofenceList;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingIntent mGeofencePendingIntent;
    LatLng latLng;
    Marker currLocationMarker;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    List<Marker> markers = new ArrayList<>();
    List<Data> markersData = new ArrayList<>();
    Location mLastLocation = null;
    Context context;

    public MapFragment() {
        // Required empty public constructor
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(connectionAddListener)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    try {
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                getGeofencingRequest(),
                                getGeofencePendingIntent()
                        ).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Log.i(TAG, "Saving Geofence");

                                } else {
                                    Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                            " : " + status.getStatusCode());
                                }
                            }
                        });

                    } catch (SecurityException securityException) {
                        // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
                        Log.e(TAG, "Error");
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {

                    Log.e(TAG, "onConnectionSuspended");

                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGeofenceList = new ArrayList<Geofence>();
        myLocationManager = new MyLocationManager(this, getContext());
        requestNeededPermission();
        context = this.getContext();
        buildGoogleApiClient();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        createGeofences();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        return view;

    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                MapPresenter.getInstance().newItemView(point);
            }
        });
        setUpMap(markersData);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.getMapAsync(this);
        mGoogleApiClient.connect();
        getData();


    }


    private void geoCode() {
       /* Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        String streetAddress = "Gázgyár utca 1, Budapest";
        List<Address> locations = null;
        try {
            locations = geocoder.getFromLocationName(streetAddress, 3);

            Toast.makeText(this, locations.get(0).getLongitude()+", "+locations.get(0).getLatitude(),
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private String revGeoCode(LatLng loc) {
        double latitude = loc.latitude;
        double longitude = loc.longitude;
        Geocoder gc = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addrs = null;
        try {
            addrs = gc.getFromLocation(latitude, longitude, 10);
            Toast.makeText(this.getContext(), addrs.get(0).getAddressLine(0) + "\n" +
                            addrs.get(0).getAddressLine(1) + "\n" +
                            addrs.get(0).getAddressLine(2),
                    Toast.LENGTH_SHORT).show();
            return addrs.get(0).getAddressLine(0) + " " +
                    addrs.get(0).getAddressLine(1) + " " +
                    addrs.get(0).getAddressLine(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == 200) {
              /*  Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());

                String distance = null;

                if (prevLoc != null) {
                    Location placeLocation = new Location("");
                    placeLocation.setLatitude(place.getLatLng().latitude);
                    placeLocation.setLongitude(place.getLatLng().longitude);
                    distance = String.valueOf(prevLoc.distanceTo(placeLocation));
                }

                if (distance != null) {
                    toastMsg+="\nDistance: "+distance;
                }

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }


    public void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this.getContext(),
                        "I need it for gps", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {
            myLocationManager.startLocatoinMonitoring();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getContext(), "FINELOC perm granted", Toast.LENGTH_SHORT).show();

                    myLocationManager.startLocatoinMonitoring();

                } else {
                    Toast.makeText(this.getContext(),
                            "FINE perm NOT granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void getData() {
        setUpMap(MapPresenter.getInstance().getNetworkData());
    }

    public void updateData() {
        MapPresenter.getInstance().updateNetworkData();
    }

    public void updateDataCallback(List<Data> list) {
        setUpMap(list);
    }

    public void postDataCallback(Data item) {
        addMarker(item);
    }


    @Override
    public void locationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng).title("Current Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            currLocationMarker = googleMap.addMarker(markerOptions);


            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(googleMap.getCameraPosition().zoom).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }


    }

    public void createGeofences() {
        for (Data d: NetworkManager.getInstance().getData()) {
            String id = UUID.randomUUID().toString();
            Geofence fence = new Geofence.Builder()
                    .setRequestId(id)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setCircularRegion(d.getLatitude(), d.getLongitude(), 3000)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .build();
            mGeofenceList.add(fence);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this.getContext(), GeofenceTransitionsIntentService.class);



        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this.getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this.getContext(), "GEO API FAILED: " +
                connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    public void setUpMap(List<Data> items) {
        markersData = new ArrayList<>(items);
        if (googleMap != null) {
            if (markers != null) {
                markers.clear();
            }
            for (Data item : markersData) {
                addMarker(item);
            }
        }


    }

    public void addMarker(Data item) {
        LatLng latLng1 = new LatLng(item.latitude, item.longitude);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng1).title(item.place);
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pencil_grey600_48dp));
             /*   MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng1).title(item.place).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));*/
        markers.add(googleMap.addMarker(markerOptions));


    }


    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void newItemView(LatLng point) {
        AddPlaceFragment anf = new AddPlaceFragment();
        Bundle bundle = new Bundle();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ROOT);
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#.0000", otherSymbols);
        bundle.putString("place", revGeoCode(point));
        bundle.putString("longitude", df.format(point.longitude));
        bundle.putString("latitude", df.format(point.latitude));
        anf.setArguments(bundle);
        anf.show(getActivity().getSupportFragmentManager(), AddPlaceFragment.TAG);
    }


    @Override
    public void onStart() {
        super.onStart();
        MapPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onStop() {

        //  myLocationManager.stopLocationMonitoring();
        super.onStop();
        MapPresenter.getInstance().detachScreen();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        googleMap.setMyLocationEnabled(true);
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng).title("Current Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currLocationMarker = googleMap.addMarker(markerOptions);

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(googleMap.getCameraPosition().zoom).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }
}
