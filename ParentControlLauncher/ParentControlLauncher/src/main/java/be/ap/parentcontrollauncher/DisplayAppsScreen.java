package be.ap.parentcontrollauncher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;
import be.ap.parentcontrollauncher.database.ContactsTable;

public class DisplayAppsScreen extends ActionBarActivity {

    //private ArrayList<Item> appList = new ArrayList<Item>();
    private GridView gridView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_apps_screen);

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_apps_screen_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case R.id.action_select_apps:
                showSettingsPage();
                return true;
            case R.id.action_register_device:
                ShowRegisterDeviceDialog();
                return true;
            case R.id.action_send_data:
                new SendJson().execute();
                return true;
            case R.id.action_request_update:
                new RequestUpdate().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettingsPage(){
        Intent showSettings = new Intent(this, SelectAppsScreen.class);
        startActivity(showSettings);
    }

    private void ShowRegisterDeviceDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Register Device");
        alertDialog.setMessage("Are you sure you want to register your device?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new SendDeviceID().execute();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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

    private class SendDeviceID extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        TelephonyManager telephonyManager;
        @Override
        protected void onPreExecute()
        {
             progressDialog = ProgressDialog.show(DisplayAppsScreen.this, "Registering Device", "Pleas wait...", true, false);
        }

        @Override
        protected Void doInBackground(Void... param) {
            telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            NetClient netClient = new NetClient("81.83.164.27", 8041);
            netClient.ConnectWithServer();
            netClient.SendDataToServer("create;" + telephonyManager.getDeviceId() +";Device Name;");
            netClient.DisConnectWithServer();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(DisplayAppsScreen.this).create();
            alertDialog.setTitle("Device Registered");
            alertDialog.setMessage("You need to enter the following code on %website% to add it to your account:\n" + telephonyManager.getDeviceId());
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    private class SendJson extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(DisplayAppsScreen.this, "Sending Data to server", "Pleas wait...", true, false);
        }

        @Override
        protected Void doInBackground(Void... param) {
            JSONHandler jsonHandler = new JSONHandler(DisplayAppsScreen.this);
            NetClient netClient = new NetClient("81.83.164.27", 8041);
            for (String json : jsonHandler.Serialize()) {
                netClient.ConnectWithServer();
                netClient.SendDataToServer("push;" + json);
                netClient.DisConnectWithServer();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            getContentResolver().delete(ParentControlContentProvider.CONTENT_URI_LOCATIONS, null, null);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Sending Completed", Toast.LENGTH_SHORT);
        }
    }

    private class RequestUpdate extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(DisplayAppsScreen.this, "Receiving Data from server", "Pleas wait...", true, false);
        }

        @Override
        protected Void doInBackground (Void... param) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceID = telephonyManager.getDeviceId();
            NetClient netClient = new NetClient("81.83.164.27", 8041);

            //GetApps
            netClient.ConnectWithServer();
            netClient.SendDataToServer("getApps;" + deviceID + ";");
            String jsonApps = netClient.ReceiveDataFromServer();
            netClient.DisConnectWithServer();

            if (jsonApps != null && jsonApps.length() > 0) {
                ContentValues values;
                JSONHandler jsonHandler = new JSONHandler(DisplayAppsScreen.this);
                RootObject rootObject = jsonHandler.Deserialize(jsonApps);

                if (rootObject.Device[0].Apps != null) {
                    for (App app : rootObject.Device[0].Apps) {
                        values = new ContentValues();
                        values.put(ApplicationsTable.COLUMN_VISIBLE, app.Visible);
                        getContentResolver().update(ParentControlContentProvider.CONTENT_URI_APPS, values, ApplicationsTable.COLUMN_ID + " = ?", new String[]{Integer.toString(app.AppId)});
                    }
                }
            }

            //GetContacts
            netClient.ConnectWithServer();
            netClient.SendDataToServer("getContacts;" + deviceID + ";");
            String jsonContacts = netClient.ReceiveDataFromServer();
            netClient.DisConnectWithServer();

            if (jsonContacts != null && jsonContacts.length() > 0) {
                ContentValues values;
                JSONHandler jsonHandler = new JSONHandler(DisplayAppsScreen.this);
                RootObject rootObject = jsonHandler.Deserialize(jsonContacts);

                if (rootObject.Device[0].Contacts != null) {
                    for (Contact contact : rootObject.Device[0].Contacts) {
                        values = new ContentValues();
                        values.put(ContactsTable.COLUMN_FIRSTNAME, contact.FirstName);
                        values.put(ContactsTable.COLUMN_LASTNAME, contact.LastName);
                        values.put(ContactsTable.COLUMN_PHONENUMBER, contact.Number);
                        values.put(ContactsTable.COLUMN_TXTMAX, contact.TxtMax);
                        values.put(ContactsTable.COLUMN_TXTAMOUNT, contact.TxtAmount);
                        values.put(ContactsTable.COLUMN_CALLMAX, contact.CallMax);
                        values.put(ContactsTable.COLUMN_CALLAMOUNT, contact.CallAmount);
                        values.put(ContactsTable.COLUMN_BLOCKED, contact.Blocked);
                        getContentResolver().update(ParentControlContentProvider.CONTENT_URI_CONTACTS, values, ContactsTable.COLUMN_ID + " = ?", new String[]{Integer.toString(contact.ContactId)});
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Receiving Completed", Toast.LENGTH_SHORT);
        }
    }
}
