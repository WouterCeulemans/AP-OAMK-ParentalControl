package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;

public class HomeScreen extends Activity {

    public LoadApplications loadApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        loadApps = new LoadApplications();
        loadApps.execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        menu.add(0, 11, 0, "Apps")
                .setIcon(android.R.drawable.ic_menu_gallery);
        menu.add(0, 12, 0, "SMS")
                .setIcon(android.R.drawable.ic_menu_send);
        menu.add(0, 13, 0, "Add App")
                .setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, 14, 0, "Show App")
                .setIcon(android.R.drawable.ic_menu_add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case 11:
                showAppsPage();
            case 12:
                showSmsApp();
            case 13:
                AddToDB();
            case 14:
                GetFromDB();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAppsPage(){
        while (loadApps.getStatus() != AsyncTask.Status.FINISHED) {}
        Intent showApps = new Intent(this, DisplayAppsScreen.class);
        startActivity(showApps);
    }

    private void showSmsApp()
    {
        //Not yet implemented

        //Intent smsApp = new Intent(this, SmsApp.class);
        //startActivity(smsApp);
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
            Applications.appsAdapter = new AppsAdapter(result.context, R.layout.row_grid, result.appList);
            Applications.appsAdapter.setNotifyOnChange(true);
        }
    }

    private class Pair
    {
        public Context context;
        public ArrayList<Item> appList;
    }

    private void AddToDB()
    {
        ContentValues values = new ContentValues();

        values.put(ApplicationsTable.COLUMN_NAME, "Eerste app");
        values.put(ApplicationsTable.COLUMN_PACKAGE, "com.example.EersteApp");
        values.put(ApplicationsTable.COLUMN_VISIBLE, 1);
        getContentResolver().insert(ParentControlContentProvider.CONTENT_URI, values);
        Toast.makeText(getApplicationContext(), "App toegevoegd", Toast.LENGTH_SHORT).show();
    }

    private void GetFromDB()
    {
        Cursor cur;
        cur = getContentResolver().query(ParentControlContentProvider.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String id = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_ID));
            String name = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_NAME));
            Toast.makeText(getApplicationContext(), id + ", " + name, Toast.LENGTH_LONG).show();
        }
    }
}
