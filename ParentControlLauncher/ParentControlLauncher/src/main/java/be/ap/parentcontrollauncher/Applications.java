package be.ap.parentcontrollauncher;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;

/**
 * Created by Wouter on 21/02/14.
 */
public class Applications {

    public static ArrayList<Item> GetInstalledApps(Context context)
    {
        ArrayList<Item> appList = new ArrayList<Item>();
        List<ApplicationInfo> myApplications;
        Bitmap APKIcon;
        Drawable icon;

        final PackageManager pm = context.getPackageManager();
        myApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : myApplications)
        {
            icon = packageInfo.loadIcon(pm);
            if(icon instanceof BitmapDrawable) {
                APKIcon = ((BitmapDrawable)icon).getBitmap();
            }
            else
            {
                Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(),icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                icon.draw(canvas);
                APKIcon = bitmap;
            }
            appList.add(new Item(APKIcon, (String) pm.getApplicationLabel(packageInfo), packageInfo.packageName, true));
        }
        return appList;
    }

    public static ArrayList<Item> GetAppsFromDB (Context context, ContentResolver contentResolver, boolean onlyVisibleApps)
    {
        ArrayList<Item> appList = new ArrayList<Item>();
        int id;
        String title;
        String packagename;
        final PackageManager pm = context.getPackageManager();

        Cursor cur;
        if (onlyVisibleApps) {
            String selectionClause = ApplicationsTable.COLUMN_VISIBLE + " > 0";
            cur = contentResolver.query(ParentControlContentProvider.CONTENT_URI_APPS, null, selectionClause, null, null);
        }
        else
        {
            cur = contentResolver.query(ParentControlContentProvider.CONTENT_URI_APPS, null, null, null, null);
        }
        if (cur.getCount() > 0) {
            while (cur.moveToNext())
            {
                id = cur.getInt(cur.getColumnIndex(ApplicationsTable.COLUMN_ID));
                title = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_TITLE));
                packagename = cur.getString(cur.getColumnIndex(ApplicationsTable.COLUMN_PACKAGE));
                int visible = cur.getInt(cur.getColumnIndex(ApplicationsTable.COLUMN_VISIBLE));
                Boolean isVisible;
                if (visible > 0)
                    isVisible = true;
                else
                    isVisible = false;

                try
                {
                    ApplicationInfo appInfo = pm.getApplicationInfo(packagename, PackageManager.GET_META_DATA);
                    appList.add(new Item(((BitmapDrawable)appInfo.loadIcon(pm)).getBitmap(), title, packagename, isVisible, id));
                }
                catch (Exception e)
                {
                    Log.d("Apps", e.getMessage());
                    Log.d("Apps", "Error while getting app from DB: " + packagename);
                }

            }
            cur.close();
        }
        return appList;
    }

    public static void InsertAppsToDB(ContentResolver contentResolver, ArrayList<Item> appList)
    {
        ContentValues values;
        for (Item item : appList)
        {
            values = new ContentValues();
            values.put(ApplicationsTable.COLUMN_TITLE, item.title);
            values.put(ApplicationsTable.COLUMN_PACKAGE,item.packageName);
            values.put(ApplicationsTable.COLUMN_VISIBLE, item.visible);
            contentResolver.insert(ParentControlContentProvider.CONTENT_URI_APPS, values);
        }
    }

    public static void UpdateAppsToDB(ContentResolver contentResolver, ArrayList<Item> appList)
    {
        ContentValues values;
        String selection;
        for (Item item : appList)
        {
            values = new ContentValues();
            selection = ApplicationsTable.COLUMN_ID + " = " + item.ID;
            values.put(ApplicationsTable.COLUMN_VISIBLE, item.visible);
            contentResolver.update(ParentControlContentProvider.CONTENT_URI_APPS, values, selection, null);
        }
    }

    public static boolean IsEmptyDB(ContentResolver contentResolver)
    {
        Cursor cursor = contentResolver.query(ParentControlContentProvider.CONTENT_URI_APPS, null, null, null, null);

        if (cursor.getCount() > 0)
        {
            cursor.close(); //nexus 5
            return false;
        }
        else
        cursor.close(); //nexus 5
            return true;
    }

    public static AppsAdapter appsAdapter;
    public static ArrayList<Item> appList;
}
