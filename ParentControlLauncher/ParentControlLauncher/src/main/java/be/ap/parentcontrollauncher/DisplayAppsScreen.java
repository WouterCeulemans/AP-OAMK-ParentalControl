package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DisplayAppsScreen extends ActionBarActivity {

    //private ArrayList<Item> appList = new ArrayList<Item>();
    private GridView gridView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_apps_screen);

        Button settingsButton = (Button) findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSettingsPage();
            }
        });

        gridView = (GridView) findViewById(R.id.gridview1);
        progressBar = (ProgressBar) findViewById(R.id.progressbar1);
        Applications.appsAdapter = new AppsAdapter(this, R.layout.row_grid, Applications.appList);
        gridView.setAdapter(Applications.appsAdapter);
        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item app = (Item) adapterView.getItemAtPosition(i);
                launchApp(app.packageName);
            }
        });
    }

    protected void launchApp(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                Toast t = Toast.makeText(getApplicationContext(), "App Not Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        menu.add(0, 11, 0, "Settings")
                .setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case 11:
                showSettingsPage();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettingsPage(){
        Intent showSettings = new Intent(this, SelectAppsScreen.class);
        startActivity(showSettings);
    }

    @Override
    public void onRestart(){
        super.onRestart();

        Log.d("TEST", "Load on Restart");

        Applications.appsAdapter.clear();
        Applications.appList = Applications.GetAppsFromDB(getApplicationContext(), getContentResolver(), true);
        for (Item item : Applications.appList)
        {
            Applications.appsAdapter.add(item);
        }
        Applications.appsAdapter.notifyDataSetChanged();
    }
}
