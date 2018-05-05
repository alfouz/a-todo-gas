package com.atodogas.brainycar.Services.Extra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

public class LocationHelper extends LocationCallback{
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Context mContext;
    private Location mLocation;

    public LocationHelper(Context context) {
        mContext = context;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);
    }

    @SuppressLint("RestrictedApi")
    public boolean onStart() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(250);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return false;
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, this, null);
        return true;
    }

    public boolean onStop() {
        mFusedLocationClient.removeLocationUpdates(this);
        return true;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        mLocation = locationResult.getLastLocation();
    }

    public double getLatitude(){
        if(mLocation != null){
            return mLocation.getLatitude();
        }

        return 0;
    }

    public double getLongitude(){
        if(mLocation != null){
          return mLocation.getLongitude();
        }

        return 0;
    }

    public double getAltitude(){
        if(mLocation != null){
            return mLocation.getAltitude();
        }

        return 0;
    }
}
