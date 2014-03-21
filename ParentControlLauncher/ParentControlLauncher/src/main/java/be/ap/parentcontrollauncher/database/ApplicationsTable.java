package be.ap.parentcontrollauncher.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Wouter on 28/02/14.
 */
public class ApplicationsTable {

    //Database table
    public static final String TABLE_APPLICATIONS = "applications";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PACKAGE = "package";
    public static final String COLUMN_VISIBLE = "visible";

    //Database creation SQL Statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_APPLICATIONS
            + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_PACKAGE + " text not null, "
            + COLUMN_VISIBLE + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(ApplicationsTable.class.getName(), "Upgrading database from version "
            + oldVersion + " to " + newVersion
            + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXIST " + TABLE_APPLICATIONS);
        onCreate(database);

    }
}
