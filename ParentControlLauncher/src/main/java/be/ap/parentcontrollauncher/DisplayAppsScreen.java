package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DisplayAppsScreen extends ActionBarActivity {

    private ArrayList<Item> appList = new ArrayList<Item>();
    private GridView gridView;
    private AppsAdapter appsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_apps_screen);

        File appListFile = new File(getFilesDir(), "app_list.txt");

        if (appListFile.exists())
        {
            //Toon enkel deze apps
            appList = Applications.GetSelectedApps(this);
        }
        else
        {
            //Laad geinstalleerde apps
            appList = Applications.GetInstalledApps(this);
            Applications.SaveVisibleApps(this, appList);
        }

        gridView = (GridView) findViewById(R.id.gridview1);
        appsAdapter = new AppsAdapter(this, R.layout.row_grid, appList);
        gridView.setAdapter(appsAdapter);
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
}
