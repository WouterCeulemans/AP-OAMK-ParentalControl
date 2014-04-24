package be.ap.parentcontrollauncher;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;
import be.ap.parentcontrollauncher.database.LocationsTable;

/**
 * Created by Wouter on 27/03/2014.
 */
public class LocationService extends IntentService implements LocationListener {

    private final Context mContext;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;       // flag for GPS status
    boolean isNetworkEnabled = false;   // flag for network status
    boolean canGetLocation = false;     // flag for location status
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    public LocationService() {
        super("LocationService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Location newLocation = getLocation();
        if (newLocation != null)
        {
            //latitude = newLocation.getLatitude();
            //longitude = newLocation.getLongitude();
            AddLocationToDB(newLocation);
            Log.d("Location", "Lat: " + newLocation.getLatitude() + "   Long: " + newLocation.getLongitude());
        }
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    Log.d("Network", "Network");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    locationManager.removeUpdates(this);
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    locationManager.removeUpdates(this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void AddLocationToDB(Location location)
    {
        ContentValues values;
        values = new ContentValues();
        values.put(LocationsTable.COLUMN_LAT, location.getLatitude());
        values.put(LocationsTable.COLUMN_LONG, location.getLongitude());
        mContext.getContentResolver().insert(ParentControlContentProvider.CONTENT_URI_LOCATIONS, values);
    }

    @Override
    public void onLocationChanged(Location location) {
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
}