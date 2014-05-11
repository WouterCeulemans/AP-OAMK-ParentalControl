package be.ap.contactapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Wouter on 8/05/2014.
 */
public class Database {
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_PHONENUMBER = "phonenumber";
    public static final String COLUMN_TXTAMOUNT = "txtamount";
    public static final String COLUMN_TXTMAX = "txtmax";
    public static final String COLUMN_CALLAMOUNT = "callamount";
    public static final String COLUMN_CALLMAX = "callmax";
    public static final String COLUMN_BLOCKED = "blocked";
    public static final Uri CONTENT_URI_CONTACTS = Uri.parse("content://be.ap.parentcontrollauncher.contentprovider/parentcontrol/contacts");

    public static void InsertToContactToDB(ContentResolver contentResolver, ArrayList<Contact> contacts)
    {
        try {

        ContentValues values;
        for (Contact contact : contacts)
        {
            values = new ContentValues();
            values.put(COLUMN_FIRSTNAME, contact.FirstName);
            values.put(COLUMN_LASTNAME, contact.LastName);
            values.put(COLUMN_PHONENUMBER, contact.PhoneNumber);
            values.put(COLUMN_TXTAMOUNT, contact.TxtAmount);
            values.put(COLUMN_TXTMAX, contact.TxtMax);
            values.put(COLUMN_CALLAMOUNT, contact.CallAmount);
            values.put(COLUMN_CALLMAX, contact.CallMax);
            values.put(COLUMN_BLOCKED, contact.Blocked);
            contentResolver.insert(CONTENT_URI_CONTACTS, values);
        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
