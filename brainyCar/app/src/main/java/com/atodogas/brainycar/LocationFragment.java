package com.atodogas.brainycar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    private static final String TAG = LocationFragment.class.getSimpleName();

    private static final int MY_PERMISSION_LOCATION_FINE = 1234;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);



                // For dropping a marker at a point on the Map
                LatLng aCientifica = new LatLng(43.333001, -8.40903);
                googleMap.addMarker(new MarkerOptions().position(aCientifica).title("Área científica").snippet("Clase del MUEI"));

                Geocoder geoCoder = new Geocoder(getContext());
                List<Address> matches = null;
                try {
                    matches = geoCoder.getFromLocation(43.333001, -8.40903, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
                Log.d("pepe", bestMatch.getPostalCode());

                Polyline line = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(43.333001, -8.40903), new LatLng(43.533001, -8.20903))
                        .width(5)
                        .color(Color.RED));*/

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(aCientifica).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                if(checkPermissionLocation()){
                    googleMap.setMyLocationEnabled(true);
                }else{
                    askPermission();
                }



            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // Check for permission to access Location
    private boolean checkPermissionLocation() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    private void askPermission() {
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                MY_PERMISSION_LOCATION_FINE
        );
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch ( requestCode ) {
            case MY_PERMISSION_LOCATION_FINE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermissionLocation())
                        googleMap.setMyLocationEnabled(true);

                } else {
                    Toast.makeText(getContext(), getText(R.string.locationPermissionUnable), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
