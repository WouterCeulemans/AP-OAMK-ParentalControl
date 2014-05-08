package be.ap.callapp;

import android.net.Uri;

/**
 * Created by Wouter on 7/05/2014.
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
}
