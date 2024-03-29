/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
 * This class manages GPS data
 */

public class LocationGPSManager implements LocationListener {

    final private DesktopSpriteService desktopSpriteService;
    private LocationManager locationManager;
    private static LocationGPSManager instance;


    private double longitude = -1, latitude = -1;
    private boolean remindLocation = false;

    public static LocationGPSManager getInstance() {
        return instance;
    }


    public LocationGPSManager(DesktopSpriteService desktopSpriteService) {
        instance = this;
        this.desktopSpriteService = desktopSpriteService;
        locationManager = (LocationManager) MainActivity.getInstance().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.getInstance(), new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 200);
        }
        else {
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        }
    }

    // Get current longitude and latitude
    public double[] getLocation() {
        return new double[]{longitude,latitude};
    }

    @Override
    public void onLocationChanged(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        System.out.println("longitude: " + longitude + " latitude: " + latitude);
        //Log.w("myApp", "longitude: " + longitude + " latitude: " + latitude);
        if (144.76<longitude && longitude<145.16 && -38.01<latitude && latitude<-37.61
                &&remindLocation ==false){
            remindLocation = true;
            desktopSpriteService.remindMelbourne();
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }

    public void setReminderLocation(boolean boo){
        this.remindLocation = boo;
    }

}
