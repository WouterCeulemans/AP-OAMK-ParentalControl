package be.ap.parentcontrollauncher.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import be.ap.parentcontrollauncher.database.ContactsTable;
import be.ap.parentcontrollauncher.database.LocationsTable;
import be.ap.parentcontrollauncher.database.ParentControlDatabaseHelper;
import be.ap.parentcontrollauncher.database.ApplicationsTable;

/**
 * Created by Wouter on 28/02/14.
 */
public class ParentControlContentProvider extends ContentProvider {

    //database
    private ParentControlDatabaseHelper database;

    //used for the UriMacher
    private static final int APPLICATIONS = 10;
    private static final int APPLICATIONS_ID = 20;
    private static final int LOCATIONS = 30;
    private static final int LOCATIONS_ID = 40;
    private static final int CONTACTS = 50;
    private static final int CONTACTS_ID = 60;

    private static final String AUTHORITY = "be.ap.parentcontrollauncher.contentprovider";

    private static final String BASE_PATH = "parentcontrol";
    private static final String PATH_APPLICATIONS = "applications";
    private static final String PATH_LOCATIONS = "locations";
    private static final String PATH_CONTACTS = "contacts";
    public static final Uri CONTENT_URI_APPS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/" + PATH_APPLICATIONS);
    public static final Uri CONTENT_URI_LOCATIONS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/" + PATH_LOCATIONS);
    public static final Uri CONTENT_URI_CONTACTS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/" + PATH_CONTACTS);
    public static final String APPLICATIONS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/applications";
    public static final String APPLICATION_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/application";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_APPLICATIONS, APPLICATIONS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_APPLICATIONS + "/#", APPLICATIONS_ID);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_LOCATIONS, LOCATIONS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_LOCATIONS + "/#", LOCATIONS_ID);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_CONTACTS, CONTACTS);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/" + PATH_CONTACTS + "/#", CONTACTS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ParentControlDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        //check if the caller has requested a column which does not exists
        checkColumns(projection);

        //Set the table
        //queryBuilder.setTables(ApplicationsTable.TABLE_APPLICATIONS);

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case APPLICATIONS:
                queryBuilder.setTables(ApplicationsTable.TABLE_APPLICATIONS);
                break;
            case APPLICATIONS_ID:
                //adding the ID to the original query
                queryBuilder.setTables(ApplicationsTable.TABLE_APPLICATIONS);
                queryBuilder.appendWhere(ApplicationsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case LOCATIONS:
                queryBuilder.setTables(LocationsTable.TABLE_LOCATIONS);
                break;
            case LOCATIONS_ID:
                queryBuilder.setTables(LocationsTable.TABLE_LOCATIONS);
                queryBuilder.appendWhere(LocationsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            case CONTACTS:
                queryBuilder.setTables(ContactsTable.TABLE_CONTACTS);
                break;
            case CONTACTS_ID:
                queryBuilder.setTables(ContactsTable.TABLE_CONTACTS);
                queryBuilder.appendWhere(ContactsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        //make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType (Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case APPLICATIONS:
                id = sqlDB.insert(ApplicationsTable.TABLE_APPLICATIONS, null, values);
                break;
            case LOCATIONS:
                id = sqlDB.insert(LocationsTable.TABLE_LOCATIONS, null, values);
                break;
            case CONTACTS:
                id = sqlDB.insert(ContactsTable.TABLE_CONTACTS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
            case APPLICATIONS:
                rowsDeleted = sqlDB.delete(ApplicationsTable.TABLE_APPLICATIONS, selection, selectionArgs);
                break;
            case APPLICATIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ApplicationsTable.TABLE_APPLICATIONS, ApplicationsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsDeleted = sqlDB.delete(ApplicationsTable.TABLE_APPLICATIONS, ApplicationsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case LOCATIONS:
                rowsDeleted = sqlDB.delete(LocationsTable.TABLE_LOCATIONS, selection, selectionArgs);
                break;
            case LOCATIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(LocationsTable.TABLE_LOCATIONS, LocationsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsDeleted = sqlDB.delete(LocationsTable.TABLE_LOCATIONS, LocationsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case CONTACTS:
                rowsDeleted = sqlDB.delete(ContactsTable.TABLE_CONTACTS, selection, selectionArgs);
                break;
            case CONTACTS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ContactsTable.TABLE_CONTACTS, ContactsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsDeleted = sqlDB.delete(ContactsTable.TABLE_CONTACTS, ContactsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update (Uri uri, ContentValues values, String selection, String[] selectionArgs){
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case APPLICATIONS:
                rowsUpdated = sqlDB.update(ApplicationsTable.TABLE_APPLICATIONS, values, selection, selectionArgs);
                break;
            case APPLICATIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ApplicationsTable.TABLE_APPLICATIONS, values, ApplicationsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsUpdated = sqlDB.update(ApplicationsTable.TABLE_APPLICATIONS, values, ApplicationsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case LOCATIONS:
                rowsUpdated = sqlDB.update(LocationsTable.TABLE_LOCATIONS, values, selection, selectionArgs);
                break;
            case LOCATIONS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(LocationsTable.TABLE_LOCATIONS, values, LocationsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsUpdated = sqlDB.update(LocationsTable.TABLE_LOCATIONS, values, LocationsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case CONTACTS:
                rowsUpdated = sqlDB.update(ContactsTable.TABLE_CONTACTS, values, selection, selectionArgs);
                break;
            case CONTACTS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ContactsTable.TABLE_CONTACTS, values,ContactsTable.COLUMN_ID + "=" + id, null);
                }
                else {
                    rowsUpdated = sqlDB.update(ContactsTable.TABLE_CONTACTS, values, ContactsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
      }

    private void checkColumns(String[] projection) {
        String[] available = {ApplicationsTable.COLUMN_VISIBLE, ApplicationsTable.COLUMN_PACKAGE, ApplicationsTable.COLUMN_TITLE, ApplicationsTable.COLUMN_ID,
                             LocationsTable.COLUMN_ID, LocationsTable.COLUMN_LAT, LocationsTable.COLUMN_LONG, ContactsTable.COLUMN_CALLMAX, ContactsTable.COLUMN_CALLAMOUNT,
                             ContactsTable.COLUMN_FIRSTNAME, ContactsTable.COLUMN_ID, ContactsTable.COLUMN_LASTNAME, ContactsTable.COLUMN_PHONENUMBER, ContactsTable.COLUMN_TXTAMOUNT,
                             ContactsTable.COLUMN_TXTMAX};

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            //check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown column in projection");
            }
        }
    }
}
