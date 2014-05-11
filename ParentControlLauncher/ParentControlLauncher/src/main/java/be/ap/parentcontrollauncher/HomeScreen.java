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
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private NetClient netClient;
    SendJson sendJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        netClient = new NetClient("81.83.164.27", 8041);

        TextView logViewer = (TextView)findViewById(R.id.trackerLog);
        logViewer.setMovementMethod(new ScrollingMovementMethod());

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
                //To Be Removed Shortly!!!
                sendJson = new SendJson();
                sendJson.execute(HomeScreen.this);
            }
        });

        Button clearLogBtn = (Button)findViewById(R.id.clearLogButton);
        clearLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView logView = (TextView)findViewById(R.id.trackerLog);
                logView.setText("");
            }
        });

        //RegisterLocationAlarmManager();
        //RegisterSendDataAlarmManager();

        insertContacts(getContentResolver(), GetAllContactsAndroid());


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

    private void RegisterLocationAlarmManager()
    {
        //AlarmManager
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("messenger", new Messenger(handler)); // Traker logging
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 1, 1000 * 60 * 1, alarmIntent);
    }

    private void RegisterSendDataAlarmManager()
    {
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SendDataService.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000 * 60 * 60 * 12, 1000 * 60 * 60 * 12, alarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        menu.add(0, 11, 0, "Apps")
                .setIcon(android.R.drawable.ic_menu_gallery);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case 11:
                showAppsPage();
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

    /*private void SendJson()
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
    }*/

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


    //Tracker logging
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            TextView logView = (TextView)findViewById(R.id.trackerLog);

            logView.append("Provider: " + reply.getString("Provider") + "; Lat: " + reply.getDouble("Lat") + "; Long: " + reply.getDouble("Long") + "\n");
        }
    };

    private class SendJson extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... param) {
            JSONHandler jsonHandler = new JSONHandler(param[0]);
            netClient.ConnectWithServer();
            for (String json : jsonHandler.Serialize()) {
                netClient.SendDataToServer("push;" + json);
            }
            netClient.DisConnectWithServer();

            return null;
        }
    }

    private void insertContacts(ContentResolver contentResolver, ArrayList<Contact> contacts)
    {
        try {

            ContentValues values;
            for (Contact contact : contacts)
            {
                values = new ContentValues();
                values.put(ContactsTable.COLUMN_FIRSTNAME, contact.FirstName);
                values.put(ContactsTable.COLUMN_LASTNAME, "achternaam");
                values.put(ContactsTable.COLUMN_PHONENUMBER, contact.PhoneNumber);
                values.put(ContactsTable.COLUMN_TXTAMOUNT, contact.TxtAmount);
                values.put(ContactsTable.COLUMN_TXTMAX, contact.TxtMax);
                values.put(ContactsTable.COLUMN_CALLAMOUNT, contact.CallAmount);
                values.put(ContactsTable.COLUMN_CALLMAX, contact.CallMax);
                values.put(ContactsTable.COLUMN_BLOCKED, contact.Blocked);
                contentResolver.insert(ParentControlContentProvider.CONTENT_URI_CONTACTS, values);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<Contact> GetAllContactsAndroid()
    {
        ArrayList<Contact> Contacts = new ArrayList<Contact>();
        Contact contact;
        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (pCur.moveToFirst()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contact = new Contact();
                        contact.FirstName = name;
                        contact.PhoneNumber = phoneNo;
                        contact.Blocked = 0;
                        contact.CallAmount = 0;
                        contact.TxtAmount = 0;
                        contact.TxtMax = 10;
                        contact.CallMax = 100;
                        Contacts.add(contact);
                    }
                }
            }
        }

        return Contacts;
    }
}
