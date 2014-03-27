package be.ap.parentcontrollauncher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wouter on 28/02/14.
 */
public class ParentControlDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "parentcontrol.db";
    private static final int DATABASE_VERSION = 1;

    public ParentControlDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ApplicationsTable.onCreate(database);
        LocationsTable.onCreate(database);
    }

    //Method is called during an upgrade of the database,
    //e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        ApplicationsTable.onUpgrade(database, oldVersion, newVersion);
        LocationsTable.onUpgrade(database, oldVersion, newVersion);
    }
}
