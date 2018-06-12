package com.atodogas.brainycar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetTripWithDataBD;
import com.atodogas.brainycar.DTOs.TripDTO;
import com.atodogas.brainycar.DTOs.TripDataDTO;
import com.atodogas.brainycar.Utils.ColoredPoint;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripDetailsActivity extends AppCompatActivity implements CallbackInterface<TripDTO> {


    MapView mMapView;
    private GoogleMap googleMap;

    private RecyclerView tripDetailsLayout;

    private static final String TAG = TripDetailsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tripDetailsLayout = findViewById(R.id.tripDetailsLayout);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        showTrip();


        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle(R.string.tripDetails);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    //Utilizando métricas que deben ser optimizadas
    public int getSegmentColorFromMetric(float rpm, float speed) {
        //Métrica planteada pero NO usada
        // rpm/speed    20 | 40 | 60 | 80 | 100 | 120 | 140 | 160
        // 1500         30   60   90   120  150   180   210   240
        // 2500         50   100  150  200  250   300   350   400
        // 3500         70   140  210  280  350   420   490   560
        // 4500         90   180  270  360  450   540   630   720
        // 5500         110  220  330  440  550   660   770   880
        // 6500         130  260  390  510  640   770   900   1130
        // 7500         150  300  450  600  750   900   1050  1300
        // 8500         170  340  510  680  850   1020  1190  1360

        //Usando esta métrica
        //rpm/speed     20-60   60-100   100-120   120-160   160-inf
        // <2500        verde   verde    verde     amarillo  rojo
        // <4000        verde   verde   amarillo  rojo      negro
        // <6000        amar    amar    rojo      negro     negro
        // +6000        rojo    rojo    rojo      negro     negro

        int color;

        if (rpm < 2500) {
            if(speed<60){
                color = Color.GREEN;
            }else{
                if(speed<100){
                    color = Color.GREEN;
                }else{
                    if(speed<120){
                        color = Color.GREEN;
                    }else{
                        if(speed<160){
                            color = Color.YELLOW;
                        }else{
                            color = Color.RED;
                        }
                    }
                }
            }
        } else if (rpm < 4000) {
            if(speed<60){
                color = Color.GREEN;
            }else{
                if(speed<100){
                    color = Color.GREEN;
                }else{
                    if(speed<120){
                        color = Color.YELLOW;
                    }else{
                        if(speed<160){
                            color = Color.RED;
                        }else{
                            color = Color.BLACK;
                        }
                    }
                }
            }
        } else if (rpm < 6000) {
            if(speed<60){
                color = Color.YELLOW;
            }else{
                if(speed<100){
                    color = Color.YELLOW;
                }else{
                    if(speed<120){
                        color = Color.RED;
                    }else{
                        if(speed<160){
                            color = Color.BLACK;
                        }else{
                            color = Color.BLACK;
                        }
                    }
                }
            }
        } else {
            if(speed<60){
                color = Color.RED;
            }else{
                if(speed<100){
                    color = Color.RED;
                }else{
                    if(speed<120){
                        color = Color.RED;
                    }else{
                        if(speed<160){
                            color = Color.BLACK;
                        }else{
                            color = Color.BLACK;
                        }
                    }
                }
            }
        }
        return color;
    }

    //Prerrequisites: Ordered list of latlng without color
    public void drawPathPolyline(List<LatLng> points) {
        if (points.size() < 2) {
            return;
        }
        googleMap.addPolyline(new PolylineOptions()
                .addAll(points)
                .color(Color.GREEN)
                .width(5));
    }


    //Prerrequisites: Ordered list of coloredPoints
    public void drawPathPolylineColoured(List<ColoredPoint> points) {
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

    private void showTrip(){
        int valueInt = getIntent().getIntExtra("idTrip", -1);
        if(valueInt>=0){
            new GetTripWithDataBD(this, getApplicationContext()).execute(valueInt);
        }else{
            ArrayList<TripDTO> trips = new ArrayList<TripDTO>();
            TripDTO emptyTrip = new TripDTO();
            emptyTrip.setStartDate(Calendar.getInstance().getTime());
            emptyTrip.setEndDate(Calendar.getInstance().getTime());
            emptyTrip.setEndPlace("Sin datos");
            emptyTrip.setFuelConsumptionAVG(0);
            emptyTrip.setHours(0);
            emptyTrip.setMinutes(0);
            emptyTrip.setId(-1);
            emptyTrip.setKms(0);
            emptyTrip.setSpeedAVG(0);
            emptyTrip.setStartPlace("Sin datos");
            trips.add(emptyTrip);

            setTripLayout(trips);
            drawMap(trips.get(0));
        }
    }

    @Override
    public void doCallback(TripDTO tripDTO) {
        ArrayList<TripDTO> trips = new ArrayList<TripDTO>();
        TripDTO trip = tripDTO;

        if(trip==null){
            TripDTO emptyTrip = new TripDTO();
            emptyTrip.setStartDate(Calendar.getInstance().getTime());
            emptyTrip.setEndDate(Calendar.getInstance().getTime());
            emptyTrip.setEndPlace("Sin datos");
            emptyTrip.setFuelConsumptionAVG(0);
            emptyTrip.setHours(0);
            emptyTrip.setMinutes(0);
            emptyTrip.setId(-1);
            emptyTrip.setKms(0);
            emptyTrip.setSpeedAVG(0);
            emptyTrip.setStartPlace("Sin datos");
            trips.add(emptyTrip);
        }else{
            trips.add(trip);
        }

        setTripLayout(trips);

        drawMap(trips.get(0));
    }

    public void setTripLayout(List<TripDTO> tripdtos){
        HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(tripdtos, new HistoricFragmentTripAdapter.OnItemClickListener() {
            @Override public void onItemClick(TripDTO item) {
            }
        });
        tripDetailsLayout.setHasFixedSize(true);
        tripDetailsLayout.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tripDetailsLayout.setLayoutManager(llm);
    }

    private void drawMap(TripDTO trip){


        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final TripDTO tripFinal = trip;
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                List<TripDataDTO> data = tripFinal.getTripData();

                List<ColoredPoint> sourcePoints = new ArrayList<>();
                for(TripDataDTO td : data){

                    sourcePoints.add(new ColoredPoint(new LatLng(td.getLatitude(),td.getLongitude()), getSegmentColorFromMetric(td.getRPM(), td.getSpeed())));
                }
                drawPathPolylineColoured(sourcePoints);

                if(data.size()>0) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    // For dropping a marker at a point on the Map
                    LatLng startTrip = new LatLng(data.get(0).getLatitude(), data.get(0).getLongitude());
                    Address addressStart = null;
                    try {
                        addressStart = geocoder.getFromLocation(startTrip.latitude, startTrip.longitude, 1).get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    googleMap.addMarker(new MarkerOptions().position(startTrip).title(getString(R.string.startTrip)).snippet((addressStart != null) ? addressStart.getAddressLine(0) : "none"));

                    LatLng endTrip = new LatLng(data.get(data.size() - 1).getLatitude(), data.get(data.size() - 1).getLongitude());
                    Address addressEnd = null;
                    try {
                        addressEnd = geocoder.getFromLocation(endTrip.latitude, endTrip.longitude, 1).get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    googleMap.addMarker(new MarkerOptions().position(endTrip).title(getString(R.string.endTrip)).snippet((addressEnd != null) ? addressEnd.getAddressLine(0) : "none"));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sourcePoints.get(0).coords).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
    }

}
