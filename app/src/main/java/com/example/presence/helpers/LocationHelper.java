package com.example.presence.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import com.example.presence.exceptions.NullLocationException;
import com.example.presence.interfaces.LocationCallback;

public class LocationHelper implements LocationListener {
    Activity activity;
    LocationManager locationManager;
    LocationCallback locationCallback;
    final int PERMISSION_ID = 42;

    public LocationHelper(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    public void startLocationCallback(LocationCallback callback)
            throws NullLocationException {
        // update current callback:
        this.locationCallback = callback;
        // prepare location manager...
        locationManager = (LocationManager)
                activity.getSystemService(Context.LOCATION_SERVICE);
        // check permissions and settings:
        if (!locationPermissionGranted()) {
            requestPermissions();
            Toast.makeText(activity.getApplicationContext(),
                    "Please grant location permissions.",
                    Toast.LENGTH_LONG).show();
            throw new NullLocationException();
        } else if (!locationIsEnabled()) {
            Toast.makeText(activity.getApplicationContext(),
                    "Please enable location in your settings.",
                    Toast.LENGTH_LONG).show();
            throw new NullLocationException();
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(activity.getApplicationContext(),
                    "Please enable GPS.",
                    Toast.LENGTH_LONG).show();
            throw new NullLocationException();
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, // get location in 0.1-second intervals
                    1, // or if the device moves more than 1 m
                    this); // object with callback method is current object
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100, // get location in 0.1-second intervals
                    1, // or if the device moves more than 1 m
                    this); // object with callback method is current object
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("LocationHelper", "Accuracy: " + location.getAccuracy());
        locationCallback.onLocation(location); // run location callback.
        locationManager.removeUpdates(this); // and stop getting locations.
    }

    public void stopLocationCallback() {
        locationManager.removeUpdates(this);
    }

    // method to check for permissions
    public boolean locationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check if location is enabled
    public boolean locationIsEnabled() {
        LocationManager locationManager
                = (LocationManager) activity.getSystemService(activity.
                getApplicationContext().LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
