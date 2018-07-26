package com.bustracker;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private CardView searchView;
    private HashMap<String  ,LatLng> allStop;

    private TextView searchTextView;

    boolean locationForFirstTime  =true;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here


            Log.d(TAG, "onLocationChanged: ");
         //   mMap.addPolygon(new PolygonOptions().add(new LatLng(location.getLatitude(),location.getLongitude()))
           //         .add(new LatLng(27.6897888,74.3848925))
            //        .fillColor(Color.BLUE).strokeWidth(4));

            if(locationForFirstTime){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                        location.getLongitude()),12));

                locationForFirstTime = false;
            }
            else {
               // mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private int REQUEST_ROUTE = 1;
    private LocationManager mLocationManager;
    private long LOCATION_REFRESH_TIME = 1;
    private float LOCATION_REFRESH_DISTANCE=1;
    private int REQUEST_PREMISSION_LOCATION = 2;
    private BusStopManager busStopManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_meny_base);
            actionBar.setTitle(null);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        allStop = new HashMap<>();

        searchView = findViewById(R.id.searchView);
        searchTextView = findViewById(R.id.busNoTextView);
        searchView.setOnClickListener(this);

        busStopManager  =BusStopManager.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        mapFragment.getMapAsync(this);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.searchView:
                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

               Intent intent = new Intent(this, SearchActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, searchView, "saearchBar");
                    startActivityForResult(intent,REQUEST_ROUTE);

                } else {
                    startActivityForResult(intent,REQUEST_ROUTE);
                }

                break;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map));
        mMap.setMaxZoomPreference(16);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.922070,75.778885),13));


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_PREMISSION_LOCATION);

        }else {
            googleMap.setMyLocationEnabled(true);

        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setTrafficEnabled(false);

       // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jprCordinate,15));
       // subscribeToUpdates();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        drawPol();

    }


    private void searchForBus(String bus) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Buses").child(bus);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMaker(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once

        String key = dataSnapshot.getKey();

        if (key!=null && key.equals("routes")){

            allStop.clear();
            Log.d(TAG, "setMarker: parsing route");

            for (DataSnapshot stop : dataSnapshot.getChildren()) {

                String stopName = stop.getKey();
                String location = (String) stop.getValue();
                String locations[] = location.split(" ");
                Double[] latlongs = new Double[2];
                latlongs[0] = Double.valueOf(locations[0]);
                latlongs[1] = Double.valueOf(locations[1]);

                LatLng latLng = new LatLng(latlongs[0], latlongs[1]);

                allStop.put(stopName, latLng);
            }

            drawPolygen();
            return;

        }




        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());

        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {

            mMarkers.put(key, mMap.addMarker(new MarkerOptions().
                    title(key).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maker)).
                    position(location)));
        } else {
            mMarkers.get(key).setPosition(location);
        }

        drawMakers();

    }



    private void drawMakers(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }


    private void removeMaker(DataSnapshot dataSnapshot){
        String key = dataSnapshot.getKey();
        mMarkers.remove(key);
        drawMakers();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_ROUTE && resultCode==RESULT_OK){

            searchTextView.setText(busStopManager.getBusNo());

            drawPolygen();

            //searchForBus(busStopManager.getBusNo());
        }
    }



    private void drawPol(){
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.BLACK);
        polygonOptions.strokeWidth(5);

        polygonOptions.add( new LatLng(26.987349, 75.854561));
        polygonOptions.add( new LatLng(26.952065, 75.841885));

        //polygonOptions.add( new LatLng(26.922960, 75.827236));
        mMap.addPolygon(polygonOptions);


    }
    private void drawPolygen() {

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.BLACK);
        polygonOptions.strokeWidth(5);




        ArrayList<BusStop> busStops = busStopManager.getSearchedBusStop();


        for (BusStop stop : busStops) {
            polygonOptions.add(stop.latLng);
            Log.d(TAG, "drawPolygen: "+stop.stopName);
        }

        mMap.addPolygon(polygonOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
}
