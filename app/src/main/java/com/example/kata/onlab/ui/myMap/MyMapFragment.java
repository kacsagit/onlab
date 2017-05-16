package com.example.kata.onlab.ui.myMap;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kata.onlab.R;
import com.example.kata.onlab.db.MyData;
import com.example.kata.onlab.network.NetworkManager;
import com.example.kata.onlab.adapter.FriendsFragmentAdapter;
import com.example.kata.onlab.ui.friendsfragment.OnMenuSelectionSetListener;
import com.example.kata.onlab.manager.MyLocationManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyMapFragment extends Fragment implements LocationListener, FriendsFragmentAdapter.MyInterface, OnMapReadyCallback, MyLocationManager.OnLocChanged, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MapScreen {

    private static final String TAG = "MyMapFragment";
    private MyLocationManager myLocationManager = null;
    Realm realm;
    RealmResults<MyData> results;
    private Location prevLoc = null;
    private MapView mapView;
    private static GoogleMap googleMap;
    List<MyData> datalist;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    double currentLatitude = 8.5565795, currentLongitude = 76.8810227;
    List<Marker> markers = new ArrayList<>();
    List<MyData> markersData = new ArrayList<>();
    Location mLastLocation = null;
    Context context;

    public MyMapFragment() {
        // Required empty public constructor
    }


    protected synchronized void buildGoogleApiClientEmpty() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datalist = new ArrayList<>();
        myLocationManager = new MyLocationManager(this, getContext());
        requestNeededPermission();
        context = this.getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        buildGoogleApiClientEmpty();
        getActivity().invalidateOptionsMenu();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My todos");
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        onAttachToParentFragment(getActivity());
        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String email = preferences.getString("Email", "");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(email + "data").build();
        // Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
        results = realm.where(MyData.class).findAll();
        markersData=new ArrayList<>(results);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_list:
                // do stuff
                mOnPlayerSelectionSetListener.onPlayerSelectionSet(id);
                /*final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FriendsFragment());
                ft.commit();*/
                return true;

        }

        return false;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap(markersData);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mapView.getMapAsync(this);
        mGoogleApiClient.connect();
        updateDataCallback(markersData);
        NetworkManager.getInstance().updateDataMy();


    }





    public void onActivityResult(int requestCode, int resultCode, Intent MyData) {
        if (requestCode == 101) {
            if (resultCode == 200) {
              /*  Place place = PlacePicker.getPlace(this, MyData);
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


    public void updateDataCallback(List<MyData> list) {
        datalist = list;
        setUpMap(datalist);


    }

    public void postDataCallback(MyData item) {
        datalist.add(item);
        addMarker(item);
    }


    @Override
    public void locationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
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


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this.getContext(), "GEO API FAILED: " +
                connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    public void setUpMap(List<MyData> items) {
        markersData = new ArrayList<>(items);
        if (googleMap != null) {
            if (markers != null) {
                markers.clear();
                googleMap.clear();
            }
            for (MyData item : markersData) {
                addMarker(item);
            }
        }


    }

    public void addMarker(MyData item) {
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

    @Override
    public void sort(int id) {
        List<MyData> temp = new ArrayList<MyData>();
        for (MyData d : datalist) {
            if (d.ownerid == id)
                temp.add(d);
        }
        setUpMap(temp);
    }

    @Override
    public void unsort() {
        setUpMap(datalist);

    }

    OnMenuSelectionSetListener mOnPlayerSelectionSetListener;


    public void onAttachToParentFragment(Activity fragment)
    {
        try
        {
            mOnPlayerSelectionSetListener = (OnMenuSelectionSetListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnMenuSelectionSetListener");
        }
    }
}
