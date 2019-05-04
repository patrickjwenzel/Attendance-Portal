package com.example.loginscreen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.loginscreen.net_utils.AppController.TAG;

/**
 * Class that gets and updates the location of users
 */
public class LocationUpdate  {

    /**
     * The current location
     */
    private static Location currentLocation;

    /**
     * Criteria used
     */
    private Criteria criteria;

    /**
     * Used to update the location
     */
    private static LocationManager locationManager;

    /**
     * The best provider
     */
    private String bestProvider;

    /**
     * Used to determine if location permissions were granted or not
     */
    private boolean mLocationPermissionsGranted;

    /**
     * FINE_LOCATION string
     */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    /**
     * COARSE_LOCATION string
     */
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    /**
     * Checks if user's location has changed
     */
    private static LocationListener locationListener;

    /**
     * Locatoni permission request code
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    /**
     * Activity the user is in
     */
    private Activity activity;

    /**
     * Given user
     */
    public  User user;

    /**
     * Creates a new location updater
     * @param activity Given activity
     * @param location Given Location
     * @param user Given user
     */
    public LocationUpdate(Activity activity, Location location, final User user){
        this.user = user;

        currentLocation = location;
        this.activity = activity;
        mLocationPermissionsGranted = false;
        statusCheck();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                user.updateLatLon((float)currentLocation.getLatitude(), (float)currentLocation.getLongitude());
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
        getLocationPermission();
    }

    /**
     * Updates the location
     */
    public void updateLocation(){
        statusCheck();
        getLocationPermission();
    }

    /**
     * Checks if the location has changed
     */
    public void statusCheck() {
        final LocationManager manager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
    }

    /**
     * Gets the user's permission to use their location
     */
    private void getLocationPermission() {
        Log.d(TAG, "Getting Location Permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                locationManager = (LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (currentLocation != null) {
                    Log.d(TAG, currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                    user.updateLatLon((float)currentLocation.getLatitude(), (float)currentLocation.getLongitude());
                } else {
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 100, 1, locationListener, null);
                }
            } else {
                ActivityCompat.requestPermissions(activity, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(activity, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Sees if permission was granted or not
     * @param requestCode Request code
     * @param permissions Array of permissions
     * @param grantResults Array of grant results
     */
    @Deprecated
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "Permission Failed");
                            return;
                        }
                    }
                    Log.d(TAG, "Permission Granted");
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    /**
     * Checks if the user has location services turned on
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
