package com.example.kata.onlab.ui.newPlace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kata.onlab.R;
import com.example.kata.onlab.db.DataDetails;
import com.example.kata.onlab.network.NetworkManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Locale;

public class PlacePickerActivity extends AppCompatActivity implements  PlacePickerScreen{
    int PLACE_PICKER_REQUEST = 1;
    Context context;
    private EditText place;
    private EditText longitude;
    private EditText latitude;
    private EditText details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        ImageButton buttonPlace= (ImageButton) findViewById(R.id.place);
        context=this;
        final Activity activity=this;
        place = (EditText) findViewById(R.id.PlaceEditText);
        longitude = (EditText) findViewById(R.id.LongitudeEditText);
        latitude = (EditText) findViewById(R.id.LatitudeEditText);
        details= (EditText) findViewById(R.id.DescriptionEditText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add new todo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Button create= (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    NetworkManager.getInstance().postData(getItem());
                }
            }
        });


    }
    private DataDetails getItem() {
        DataDetails item = new DataDetails();
        item.place = place.getText().toString();
        item.latitude = Float.parseFloat(latitude.getText().toString());
        item.longitude = Float.parseFloat(longitude.getText().toString());
        item.description=details.getText().toString();
        //      item.save();
        return item;
    }


    private boolean isValid() {

        if ((place.getText().toString()).trim().length() == 0) {
            place.setError("Can't be empty");
            place.requestFocus();
            return false;
        }
        if ((longitude.getText().toString()).trim().length() == 0 ||Double.parseDouble(longitude.getText().toString()) <  -180.0  || Double.valueOf(longitude.getText().toString()) > 180.0) {
            longitude.setError("From -180 to 180");
            longitude.requestFocus();
            return false;
        }
        if ((latitude.getText().toString()).trim().length() == 0 || Double.valueOf(latitude.getText().toString()) < -90.0 || Double.valueOf(latitude.getText().toString()) > 90.0) {
            latitude.setError("From -90 to 90");
            latitude.requestFocus();
            return false;
        }
        if ((latitude.getText().toString()).trim().length() == 0 || Double.valueOf(latitude.getText().toString()) < -90.0 || Double.valueOf(latitude.getText().toString()) > 90.0) {
            latitude.setError("From -90 to 90");
            latitude.requestFocus();
            return false;
        }
        if ((details.getText().toString()).trim().length() == 0) {
            details.setError("Can't be empty");
            details.requestFocus();
            return false;
        }


        return true;

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place pickPlace = PlacePicker.getPlace(context, data);
                latitude.setText(String.format(Locale.ROOT,"%f",pickPlace.getLatLng().latitude));
                longitude.setText(String.format(Locale.ROOT,"%f",pickPlace.getLatLng().longitude));
                place.setText(pickPlace.getName());
            }
        }
    }

    @Override
    public void postDataCallback(DataDetails data) {
        onBackPressed();
    }


    @Override
    public void onStart() {
        super.onStart();
        PlacePickerPresenter.getInstance().attachScreen(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PlacePickerPresenter.getInstance().detachScreen();
    }

}
