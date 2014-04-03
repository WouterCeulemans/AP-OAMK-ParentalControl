package be.ap.parentcontrollauncher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.telephony.TelephonyManager;
import android.util.JsonWriter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;
import be.ap.parentcontrollauncher.database.ContactsTable;
import be.ap.parentcontrollauncher.database.LocationsTable;

/**
 * Created by Wouter on 2/04/2014.
 */
public class JSONHandler {

    private Context context;
    private TelephonyManager telephonyManager;
    public JSONHandler (Context c) {
        context = c;
        telephonyManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
    }

    public String Serialize()
    {
        Gson gson = new Gson();
        RootObject rootObject = new RootObject();
        rootObject.DeviceID = telephonyManager.getDeviceId();
        rootObject.Apps = getApps();
        rootObject.Locations = getLocations();
        //rootObject.Contacts = getContacts();

        String json = gson.toJson(rootObject);
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        jsonObj.addProperty("DeviceID", rootObject.DeviceID);
        return jsonObj.toString();
    }

    public RootObject Deserialize (String json)
    {
        Gson gson = new Gson();
        RootObject rootObject = gson.fromJson(json, RootObject.class);
        return rootObject;
    }

    private ArrayList<App> getApps()
    {
        ArrayList<App> apps = new ArrayList<App>();
        App app = new App();
        final PackageManager pm = context.getPackageManager();

        Cursor cur = context.getContentResolver().query(ParentControlContentProvider.CONTENT_URI_APPS, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext())
            {
                app = new App();
                app.AppID = cur.getInt(cur.getColumnIndex(ApplicationsTable.COLUMN_ID));
                app.Title = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_TITLE));
                app.PackageName = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_PACKAGE));
                app.Visible = cur.getInt(cur.getColumnIndex(ApplicationsTable.COLUMN_VISIBLE));

                apps.add(app);
            }
            cur.close();
        }
        return apps;
    }

    private ArrayList<Location> getLocations()
    {
        ArrayList<Location> locations = new ArrayList<Location>();
        Location location = new Location();

        Cursor cur =  context.getContentResolver().query(ParentControlContentProvider.CONTENT_URI_LOCATIONS, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext())
            {
                location = new Location();
                location.PosID = cur.getInt(cur.getColumnIndex(LocationsTable.COLUMN_ID));
                location.Latitude = cur.getDouble(cur.getColumnIndex(LocationsTable.COLUMN_LAT));
                location.Longitude = cur.getDouble(cur.getColumnIndex(LocationsTable.COLUMN_LONG));

               locations.add(location);
            }
            cur.close();
        }
        return locations;
    }

    private ArrayList<Contact> getContacts()
    {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Contact contact = new Contact();

        Cursor cur = context.getContentResolver().query(ParentControlContentProvider.CONTENT_URI_CONTACTS, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext())
            {
                contact = new Contact();
                contact.ContactID = cur.getInt(cur.getColumnIndex(ContactsTable.COLUMN_ID));
                contact.FirstName = cur.getString(cur.getColumnIndex(ContactsTable.COLUMN_FIRSTNAME));
                contact.LastName = cur.getString(cur.getColumnIndex(ContactsTable.COLUMN_LASTNAME));
                contact.PhoneNumber = cur.getString(cur.getColumnIndex(ContactsTable.COLUMN_PHONENUMBER));
                contact.TxtAmount = cur.getInt(cur.getColumnIndex(ContactsTable.COLUMN_TXTAMOUNT));
                contact.TxtMax = cur.getInt(cur.getColumnIndex(ContactsTable.COLUMN_TXTMAX));
                contact.CallAmount = cur.getInt(cur.getColumnIndex(ContactsTable.COLUMN_CALLAMOUNT));
                contact.CallMax = cur.getInt(cur.getColumnIndex(ContactsTable.COLUMN_CALLMAX));

                contacts.add(contact);
            }
            cur.close();
        }
        return contacts;
    }
}
