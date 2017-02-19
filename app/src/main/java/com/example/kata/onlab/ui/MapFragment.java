package com.example.kata.onlab.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.DataManager;
import com.example.kata.onlab.MyLocationManager;
import com.example.kata.onlab.R;
import com.example.kata.onlab.event.ErrorEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MyLocationManager.OnLocChanged,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private MyLocationManager myLocationManager = null;

    private Location prevLoc = null;
    private MapView mapView;
    private static GoogleMap googleMap;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;

    List<Marker> markers = new ArrayList<>();
    Location mLastLocation = null;

    private GoogleApiClient googleApiClient;

    public MapFragment() {
        // Required empty public constructor
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myLocationManager = new MyLocationManager(this, getContext());
        requestNeededPermission();
        buildGoogleApiClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 1 second, in milliseconds


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);

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
                AddPlaceFragment anf=new AddPlaceFragment();
                Bundle bundle = new Bundle();
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ROOT);
                otherSymbols.setGroupingSeparator('.');
                DecimalFormat df = new DecimalFormat("#.0000",otherSymbols);
                bundle.putString("longitude",df.format(point.longitude));
                bundle.putString("latitude",df.format(point.latitude));
                anf.setArguments(bundle);
                anf.show(getActivity().getSupportFragmentManager(),AddPlaceFragment.TAG);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.getMapAsync(this);
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
        loadData();


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

    private void revGeoCode() {
      /*  if (prevLoc != null) {
            double latitude = prevLoc.getLatitude();
            double longitude = prevLoc.getLongitude();
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            List<Address> addrs = null;
            try {
                addrs = gc.getFromLocation(latitude, longitude, 10);
                Toast.makeText(this, addrs.get(0).getAddressLine(0)+"\n"+
                                addrs.get(0).getAddressLine(1)+"\n"+
                                addrs.get(0).getAddressLine(2),
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void showPlacePicker() {
       /* PlacePicker.IntentBuilder builder =
                new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(MainActivity.this), 101);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }*/
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



    @Override
    public void onStop() {
        super.onStop();
        myLocationManager.stopLocationMonitoring();
    }

    @Override
    public void locationChanged(Location location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng).title("Current Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currLocationMarker = googleMap.addMarker(markerOptions);



            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(googleMap.getCameraPosition().zoom).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this.getContext(), "GEO API FAILED: " +
                connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    public  void setUpMap(List<Data> items) {
        if (googleMap != null) {
            if (markers != null) {
                markers.clear();
            }
            for (Data item : items) {
                LatLng latLng1 = new LatLng(item.latitude, item.longitude);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng1).title(item.place);
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pencil_grey600_48dp));
             /*   MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng1).title(item.place).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));*/
                markers.add(googleMap.addMarker(markerOptions));
            }
        }


    }

    public void loadData() {
        DataManager.getInstance(this.getContext()).getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetData(GetDataEvent<List<Data>> event) {
        setUpMap(event.getData());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        event.getE().printStackTrace();
    }



    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);
        super.onPause();


    }
    @Override
    public void onConnectionSuspended(int i) {

    }
}
