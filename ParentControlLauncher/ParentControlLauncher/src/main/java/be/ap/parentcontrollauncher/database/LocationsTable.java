package be.ap.parentcontrollauncher.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Wouter on 26/03/2014.
 */
public class LocationsTable {

    //Database table
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LONG = "longitude";
    public static final String COLUMN_LAT = "latitude";

    //Database creation SQL Statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_LOCATIONS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LONG + " text not null, "
            + COLUMN_LAT + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ApplicationsTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXIST " + TABLE_LOCATIONS);
        onCreate(database);
    }
}
