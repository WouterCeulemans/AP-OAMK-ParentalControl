package be.ap.parentcontrollauncher.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Wouter on 2/04/2014.
 */
public class ContactsTable {

    //Database table
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_PHONENUMBER = "phonenumber";
    public static final String COLUMN_TXTAMOUNT = "txtamount";
    public static final String COLUMN_TXTMAX = "txtmax";
    public static final String COLUMN_CALLAMOUNT = "callamount";
    public static final String COLUMN_CALLMAX = "callmax";

    //Database creation SQL Statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACTS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_FIRSTNAME + " text not null, "
            + COLUMN_LASTNAME + " text not null, "
            + COLUMN_PHONENUMBER + " text not null, "
            + COLUMN_TXTAMOUNT + " integer not null, "
            + COLUMN_TXTMAX + " integer not null, "
            + COLUMN_CALLAMOUNT + " integer not null, "
            + COLUMN_CALLMAX + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ApplicationsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXIST " + TABLE_CONTACTS);
        onCreate(database);

    }
}

