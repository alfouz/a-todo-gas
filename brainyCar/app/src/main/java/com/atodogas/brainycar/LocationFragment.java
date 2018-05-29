package com.atodogas.brainycar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetCarBD;
import com.atodogas.brainycar.DTOs.CarDTO;
import com.atodogas.brainycar.FuelStation.FuelStation;
import com.atodogas.brainycar.FuelStation.FuelStationParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private CarDTO car;
    private ArrayList<FuelStation> fuelStations;

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
        fuelStations = new ArrayList<>();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GetCarBD(new CallbackInterface<CarDTO>() {
            @Override
            public void doCallback(CarDTO carDto) {
                car = carDto;
            }
        }, getActivity().getApplicationContext()).execute(getActivity().getIntent().getIntExtra("idUser", -1));


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getActivity().getApplicationContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getActivity().getApplicationContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getActivity().getApplicationContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                // For dropping a marker at a point on the Map
                //LatLng aCientifica = new LatLng(43.333001, -8.40903);
                //googleMap.addMarker(new MarkerOptions().position(aCientifica).title("Área científica").snippet("Clase del MUEI"));


                //Example usage draw path
                /*List<ColoredPoint> sourcePoints = new ArrayList<>();
                for(int i = 0; i<50000; i++){
                    sourcePoints.add(new ColoredPoint(new LatLng(43.333001 +(i*0.0001),-8.40903+(i*0.0001)), getSegmentColorFromMetric(i%10000)));
                    //sourcePoints.add(new LatLng(43.333001 +(i*0.0001),-8.40903+(i*0.0001)));
                }
                drawPathPolylineColoured(sourcePoints);*/


                if (checkPermissionLocation()) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    askPermission();
                }

            }
        });

        FloatingActionButton fuelStationsButton = view.findViewById(R.id.fuelStationsButton);
        fuelStationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFuelStationsAPI();
            }
        });

        FloatingActionButton carLocationButton = view.findViewById(R.id.carLocationButton);
        carLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastCarPosition(car);
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
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_LOCATION_FINE
        );
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
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


    //Set a car image on latlng indicated
    private void setLastCarPosition(CarDTO car) {
        if(car == null || car.getLatitude() == 999 || car.getLongitude() == 999){
            return;
        }

        googleMap.clear();
        LatLng position = new LatLng(car.getLatitude(), car.getLongitude());
        //int height = 200;
        //int width = 200;

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_map_marker);
        Bitmap b = bitmapdraw.getBitmap();


        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(b);

        Marker mCar = googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(getString(R.string.titleLastLocationCar))
                .snippet(getString(R.string.snippetLastLocationCar) + " " + position.latitude + "," + position.longitude)
                .icon(icon));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //TODO Need update this function
    private int getSegmentColorFromMetric(float metric) {
        int color;
        if (metric < 2000) {
            color = Color.BLUE;
        } else if (metric < 5000) {
            color = Color.GREEN;
        } else if (metric < 7500) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }
        return color;
    }

    //Prerrequisites: Ordered list of latlng without color
    private void drawPathPolyline(List<LatLng> points) {
        if (points.size() < 2) {
            return;
        }
        googleMap.addPolyline(new PolylineOptions()
                .addAll(points)
                .color(Color.GREEN)
                .width(5));
    }


    //Prerrequisites: Ordered list of coloredPoints
    private void drawPathPolylineColoured(List<ColoredPoint> points) {
        if (points.size() < 2) {
            return;
        }

        int ix = 0;
        ColoredPoint currentPoint = points.get(ix);
        int currentColor = currentPoint.color;
        List<LatLng> currentSegment = new ArrayList<>();
        currentSegment.add(currentPoint.coords);
        ix++;

        while (ix < points.size()) {
            currentPoint = points.get(ix);

            if (currentPoint.color == currentColor) {
                currentSegment.add(currentPoint.coords);
            } else {
                currentSegment.add(currentPoint.coords);
                googleMap.addPolyline(new PolylineOptions()
                        .addAll(currentSegment)
                        .color(currentColor)
                        .width(5));
                currentColor = currentPoint.color;
                currentSegment.clear();
                currentSegment.add(currentPoint.coords);
            }

            ix++;
        }

        googleMap.addPolyline(new PolylineOptions()
                .addAll(currentSegment)
                .color(currentColor)
                .width(5));
    }

    private void getFuelStationsAPI(){
        if(fuelStations.size() <= 0){
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            Address address = null;

            try {
                address = geocoder.getFromLocation(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude(),1).get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(address == null){
                if(car != null && car.getLongitude() < 999 && car.getLatitude() < 999){
                    try {
                        address = geocoder.getFromLocation(car.getLatitude(), car.getLongitude(),1).get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            if(address != null){
                String uri = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroProvincia/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams rp = new RequestParams();
                client.get(uri + address.getPostalCode().substring(0,2), rp, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        FuelStationParser fuelStationParser = new FuelStationParser();
                        fuelStations = fuelStationParser.parserJson(response);

                        updateFuelStationsMap();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        }
        else {
            updateFuelStationsMap();
        }
    }

    private void updateFuelStationsMap(){
        googleMap.clear();

        for(FuelStation f : fuelStations){
            String snipet = "Horario: " + f.getTime() + "\n";
            snipet += "Diesel A: " + f.getDieselA() + "€\n";
            snipet += "Gasolina 95: " + f.getGasoline95() + "€\n";

            if(f.getGasoline98() > 0){
                snipet += "Gasolina 98: " + f.getGasoline98() + "€\n";
            }

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(f.getLatitude(), f.getLongitude()))
                    .title(f.getTitle())
                    .snippet(snipet));
        }
    }

    //Auxiliar class to get points coloured
    class ColoredPoint {
        public LatLng coords;
        public int color;

        public ColoredPoint(LatLng coords, int color) {
            this.coords = coords;
            this.color = color;
        }
    }

}

