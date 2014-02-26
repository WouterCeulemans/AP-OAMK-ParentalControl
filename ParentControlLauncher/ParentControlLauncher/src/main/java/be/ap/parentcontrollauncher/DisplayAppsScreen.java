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
    private AppsAdapter appsAdapter;
    private ProgressBar progressBar;
    private ArrayList<Item> listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_apps_screen);

        gridView = (GridView) findViewById(R.id.gridview1);
        progressBar = (ProgressBar) findViewById(R.id.progressbar1);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item app = (Item) adapterView.getItemAtPosition(i);
                launchApp(app.packageName);
            }
        });
        new LoadApplications().execute(this);
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

        appsAdapter.clear();
        listApps = Applications.GetSelectedApps(this);
        for (Item item : listApps)
        {
            appsAdapter.add(item);
        }
        appsAdapter.notifyDataSetChanged();
    }

    private class LoadApplications extends AsyncTask<Context, Void, Pair>
    {
        @Override
        protected Pair doInBackground(Context... param)
        {
            ArrayList<Item> apps;
            Pair p = new Pair();
            p.context = param[0];
            File appListFile = new File(getFilesDir(), "app_list.txt");

            if (appListFile.exists())
            {
                //Toon enkel deze apps
                apps = Applications.GetSelectedApps(param[0]);
            }
            else
            {
                //Laad geinstalleerde apps
                apps = Applications.GetInstalledApps(param[0]);
                Applications.SaveVisibleApps(param[0], apps);
            }
            p.appList = apps;
            return p;
        }

        protected void onPostExecute(Pair result) {
            appsAdapter = new AppsAdapter(result.context, R.layout.row_grid, result.appList);
            appsAdapter.setNotifyOnChange(true);
            gridView.setAdapter(appsAdapter);
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    private class Pair
    {
        public Context context;
        public ArrayList<Item> appList;
    }
}
