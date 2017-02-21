package com.example.kata.onlab.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kata.onlab.Data;
import com.example.kata.onlab.R;

public class AddPlaceFragment extends AppCompatDialogFragment {
    public static final String TAG = "AddPlaceFragment";

    private EditText place;
    private EditText longitude;
    private EditText latitude;

    private IAddPlaceFragment listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context activity = getContext();
        if (activity instanceof IAddPlaceFragment) {
            listener = (IAddPlaceFragment) activity;
        } else {
            throw new RuntimeException("Activity must implement the IAddPlaceFragment interface!");
        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog d = new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        if (getArguments()!=null) {
            place.setText((getArguments().getString("place")));
            longitude.setText((getArguments().getString("longitude")));
            latitude.setText((getArguments().getString("latitude")));
        }


        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValid()) {
                            listener.onNewItemCreated(getItem());
                            d.dismiss();

                        }
                    }
                });
            }
        });
        return d;
    }

    public interface IAddPlaceFragment {
        void onNewItemCreated(Data newItem);
    }

    private boolean isValid() {
        if ((place.getText().toString()).trim().length() == 0) {
            place.setError("Can't be empty");
            place.requestFocus();
            return false;
        }
        if ((longitude.getText().toString()).trim().length() == 0 || Double.parseDouble(longitude.getText().toString()) < -180.0 || Double.parseDouble(longitude.getText().toString()) > 180.0) {
            longitude.setError("From -180 to 180");
            longitude.requestFocus();
            return false;
        }
        if ((latitude.getText().toString()).trim().length() == 0 || Double.parseDouble(latitude.getText().toString()) < -90.0 || Double.parseDouble(latitude.getText().toString()) > 90.0) {
            latitude.setError("From -90 to 90");
            latitude.requestFocus();
            return false;
        }


        return true;

    }

    private Data getItem() {
        Data item = new Data();
        item.place = place.getText().toString();
        item.latitude = Float.parseFloat(latitude.getText().toString());
        item.longitude = Float.parseFloat(longitude.getText().toString());
  //      item.save();
        return item;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_place, null);
        place = (EditText) contentView.findViewById(R.id.PlaceEditText);
        longitude = (EditText) contentView.findViewById(R.id.LongitudeEditText);
        latitude = (EditText) contentView.findViewById(R.id.LatitudeEditText);


        return contentView;
    }

}
