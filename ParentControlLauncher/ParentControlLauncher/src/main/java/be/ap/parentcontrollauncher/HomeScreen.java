package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;
import be.ap.parentcontrollauncher.database.ContactsTable;

public class HomeScreen extends Activity {

    public LoadApplications loadApps;
    private Pair p = new Pair();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private NetClient netClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button smsAppBtn = (Button)findViewById(R.id.SmsButton);
        smsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSmsApp();
            }
        });

        Button callAppBtn = (Button)findViewById(R.id.CallButton);
        callAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCallApp();
            }
        });

        Button contactAppBtn = (Button)findViewById(R.id.ContactButton);
        contactAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactApp();
            }
        });

        Button sendJsonBtn = (Button)findViewById(R.id.SendButton);
        sendJsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendJson();
            }
        });

        Button sendIDBtn = (Button)findViewById(R.id.SendIDButton);
        sendIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendIDDevice();
            }
        });

        //AlarmManager
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocationService.class);
        alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 15, 1000 * 60 * 15, alarmIntent);

        Button appButton = (Button)findViewById(R.id.AppButton);
        appButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAppsPage();
            }
        });

        p.context = getApplicationContext();
        p.contentResolver = getContentResolver();

        netClient = new NetClient("81.83.164.27", 8041);

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

    private void openSmsApp() {
        PackageManager pm = this.getPackageManager();
        String packageName = "be.ap.smsapp";
        try
        {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                Toast t = Toast.makeText(getApplicationContext(), "App Not Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not open App", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void openCallApp() {
        PackageManager pm = this.getPackageManager();
        String packageName = "be.ap.callapp";
        try
        {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                Toast t = Toast.makeText(getApplicationContext(), "App Not Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not open App", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void openContactApp() {
        PackageManager pm = this.getPackageManager();
        String packageName = "be.ap.contactapp";
        Boolean isInstalled;
        try
        {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            try {
                startActivity(mIntent);
            } catch (ActivityNotFoundException err) {
                Toast t = Toast.makeText(getApplicationContext(), "App Not Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not open App", Toast.LENGTH_SHORT);
            toast.show();
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

    private void SendJson()
    {
        JSONHandler jsonHandler = new JSONHandler(this);
        netClient.ConnectWithServer();
        netClient.SendDataToServer("push;" + jsonHandler.Serialize());
        netClient.DisConnectWithServer();
    }

    private void SendIDDevice()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        netClient.ConnectWithServer();
        netClient.SendDataToServer("create;" + telephonyManager.getDeviceId() +";Device Name;");
        netClient.DisConnectWithServer();
    }

    private void RequestUpdate()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        netClient.ConnectWithServer();
        netClient.SendDataToServer("get;" + telephonyManager.getDeviceId() +";");
        String json = netClient.ReceiveDataFromServer();
        netClient.DisConnectWithServer();

        if (json != null && json.length() > 0)
        {
            ContentValues values;
            String selection;
            JSONHandler jsonHandler = new JSONHandler(this);
            RootObject data = jsonHandler.Deserialize(json);

            for (App app : data.Apps)
            {
                values = new ContentValues();
                selection = ApplicationsTable.COLUMN_ID + " = " + app.AppID;
                values.put(ApplicationsTable.COLUMN_VISIBLE, app.Visible);
                getContentResolver().update(ParentControlContentProvider.CONTENT_URI_APPS, values, selection, null);
            }
            for (Contact contact : data.Contacts)
            {
                values = new ContentValues();
                selection = ContactsTable.COLUMN_ID + " = " + contact.ContactID;
                values.put(ContactsTable.COLUMN_FIRSTNAME, contact.FirstName);
                values.put(ContactsTable.COLUMN_LASTNAME, contact.LastName);
                values.put(ContactsTable.COLUMN_PHONENUMBER, contact.PhoneNumber);
                values.put(ContactsTable.COLUMN_TXTMAX, contact.TxtMax);
                values.put(ContactsTable.COLUMN_TXTAMOUNT, contact.TxtAmount);
                values.put(ContactsTable.COLUMN_CALLMAX, contact.CallMax);
                values.put(ContactsTable.COLUMN_CALLAMOUNT, contact.CallAmount);
                getContentResolver().update(ParentControlContentProvider.CONTENT_URI_CONTACTS, values, selection, null);
            }

            //Locations ??? Remove or not
        }
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
