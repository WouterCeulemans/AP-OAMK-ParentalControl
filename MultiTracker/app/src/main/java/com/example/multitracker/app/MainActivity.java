package com.example.multitracker.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
    Button btnShowLocation;
    //GPS Tracker class

    TrackingServices tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button)findViewById(R.id.btnShowLocation);

        //show Location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //create class object
                tracking = new TrackingServices(MainActivity.this);

                //check if GPS enabled
                if (tracking.canGetLocation())
                {
                    double latitude = tracking.getLatitude();
                    double longitude = tracking.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "YourLocation is -\n: " + latitude + " \n long: "+ longitude, Toast.LENGTH_LONG).show();
                }
                else
                {
                    tracking.showToast();
                }

            }
        });
    }
}
