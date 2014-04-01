package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;

public class HomeScreen extends Activity {

    public LoadApplications loadApps;
    private Pair p = new Pair();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //AlarmManager
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocationService.class);
        alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 1, 1000 * 60 * 1, alarmIntent);

        Button appButton = (Button)findViewById(R.id.AppButton);
        appButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAppsPage();
            }
        });

        p.context = getApplicationContext();
        p.contentResolver = getContentResolver();

        loadApps = new LoadApplications();
        loadApps.execute(p);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        menu.add(0, 11, 0, "Apps")
                .setIcon(android.R.drawable.ic_menu_gallery);
        menu.add(0, 12, 0, "Send app intent")
                .setIcon(android.R.drawable.ic_menu_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case 11:
                showAppsPage();
            case 12:
                //showSmsApp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAppsPage(){
        if (loadApps.getStatus() != AsyncTask.Status.FINISHED) {
            Toast.makeText(getApplicationContext(), "Your apps are still loading", Toast.LENGTH_LONG).show();
        }
        else {
            Intent showApps = new Intent(this, DisplayAppsScreen.class);
            startActivity(showApps);
        }
    }

    private class LoadApplications extends AsyncTask<Pair, Void, Pair>
    {
        @Override
        protected Pair doInBackground(Pair... param)
        {
            ArrayList<Item> apps;

            boolean emptyDB = Applications.IsEmptyDB(getContentResolver());
            if (emptyDB)
            {
                apps = Applications.GetInstalledApps(param[0].context);
                Applications.InsertAppsToDB(param[0].contentResolver, apps);
            }
            else
            {
                apps = Applications.GetAppsFromDB(param[0].context, param[0].contentResolver, true);
            }

            param[0].appList = apps;
            return param[0];
        }

        protected void onPostExecute(Pair result) {
            Applications.appList = result.appList;
            //Applications.appsAdapter = new AppsAdapter(result.context, R.layout.row_grid, result.appList);
            //Applications.appsAdapter.setNotifyOnChange(true);
        }
    }

    private class Pair
    {
        public Context context;
        public ContentResolver contentResolver;
        public ArrayList<Item> appList;
    }

   /* private void AddToDB()
    {
        ContentValues values = new ContentValues();

        values.put(ApplicationsTable.COLUMN_TITLE, "Eerste app");
        values.put(ApplicationsTable.COLUMN_PACKAGE, "com.example.EersteApp");
        values.put(ApplicationsTable.COLUMN_VISIBLE, 1);
        getContentResolver().insert(ParentControlContentProvider.CONTENT_URI_APPS, values);
        Toast.makeText(getApplicationContext(), "App toegevoegd", Toast.LENGTH_SHORT).show();
    }

    private void GetFromDB()
    {
        try {
            Cursor cur;
            cur = getContentResolver().query(ParentControlContentProvider.CONTENT_URI_APPS, null, null, null, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                String id = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_ID));
                String name = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_TITLE));
                Toast.makeText(getApplicationContext(), id + ", " + name, Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Geen gegevens", Toast.LENGTH_SHORT).show();
        }
    }*/
}
