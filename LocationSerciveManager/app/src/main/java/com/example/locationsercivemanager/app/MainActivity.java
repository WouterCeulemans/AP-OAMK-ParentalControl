package com.example.locationsercivemanager.app;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    TextView TxtLat;
    TextView TxtLong;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TxtLat = (TextView) findViewById(R.id.TxtLat);
        TxtLong = (TextView) findViewById(R.id.TxtLong);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // implementation of TOASTS
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast toast= Toast.makeText(MainActivity.this, " You can improve the accuracy by turning on the GPS", Toast.LENGTH_SHORT);
            toast.show();
        }
        LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);


    }

        class myLocationListener implements LocationListener
        {

            @Override
            public void onLocationChanged(Location location)
            {
                if (location != null)
                {
                    double pLong = location.getLongitude();
                    double pLat = location.getLatitude();

                    TxtLat.setText(Double.toString(pLat));
                    TxtLong.setText(Double.toString(pLong));
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
        }

    }

