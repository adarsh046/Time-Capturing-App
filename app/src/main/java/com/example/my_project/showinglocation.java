package com.example.my_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import java.sql.Time;
import java.util.Calendar;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class showinglocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Toast toast;
    Boolean flag = false;
    DatabaseHelper db;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinglocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; //  6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;


        return dist; // output distance, in KM
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(showinglocation.this, SecondActivity.class));
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        Calendar calendar = Calendar.getInstance();
        int currentday = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("Prefs", 0);
        int lastday = settings.getInt("day", 0);
        db = new DatabaseHelper(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        Location lastknown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng user = new LatLng(lastknown.getLatitude(), lastknown.getLongitude());
        double curlat=lastknown.getLatitude();
        double curlon=lastknown.getLongitude();

        if(currentday!=lastday){
         if (distance(curlat, curlon, 28.6209202, 77.3536615) < 0.01) { // if distance < 0.01 miles we take locations as equal, Second Location Artech pvt. ltd.

            String currentTime = Calendar.getInstance().getTime().toString();
            boolean isinserted=db.insertdata(currentTime.toString());
            if(isinserted==true){
                Toast.makeText(showinglocation.this,"Location Matched "+ currentTime +" Database Updated",Toast.LENGTH_SHORT).show();
                flag=true;
                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("day",currentday);
                editor.commit();
            }
            else
                Toast.makeText(showinglocation.this,"Error in updating database",Toast.LENGTH_SHORT).show();
            flag=false;


        }
        else {
            Toast.makeText(showinglocation.this,"Location Not Matched",Toast.LENGTH_SHORT).show();
        }}
        else
            Toast.makeText(showinglocation.this,"Database may have already updated",Toast.LENGTH_SHORT).show();






        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                double currentLat=location.getLatitude();
                double currentLon=location.getLongitude();
                LatLng user = new LatLng(currentLat, currentLon);
                mMap.addMarker(new MarkerOptions().position(user).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,18));








            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if(Build.VERSION.SDK_INT<23){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 ,0 ,locationListener);

        }
        else {

           if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else{

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 ,0 ,locationListener);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(user).title("Your Location"));
              mMap.moveCamera(CameraUpdateFactory.newLatLng(user));


            }

        }





    }
}
