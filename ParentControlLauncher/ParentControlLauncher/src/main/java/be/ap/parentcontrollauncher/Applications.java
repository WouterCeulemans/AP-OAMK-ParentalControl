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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (!Arrays.asList(SystemApps).contains(packageInfo.packageName)) {
                icon = packageInfo.loadIcon(pm);
                if (icon instanceof BitmapDrawable) {
                    APKIcon = ((BitmapDrawable) icon).getBitmap();
                } else {
                    Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    icon.draw(canvas);
                    APKIcon = bitmap;
                }
                appList.add(new Item(APKIcon, (String) pm.getApplicationLabel(packageInfo), packageInfo.packageName, true));
            }
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
    public static String[] SystemApps =
    {
            "android",
            "android.tts",
            "be.ap.parentcontrollauncher",
            "com.android.audioTuning",
            "com.android.bluetooth",
            "com.android.certinstaller",
            "com.android.defcontainer",
            "com.android.packageinstaller",
            "com.android.Preconfig",
            "com.android.providers.applications",
            "com.android.providers.calendar",
            "com.android.providers.contacts",
            "com.android.providers.downloads",
            "com.android.providers.media",
            "com.android.providers.settings",
            "com.android.providers.subscribedfeeds",
            "com.android.providers.telephony",
            "com.android.providers.userdictionary",
            "com.android.RilFactoryApp",
            "com.android.samsungtest.DataCreate",
            "com.android.server.vpn",
            "com.android.settings.mt",
            "com.android.setupwizard",
            "com.android.vending.updater",
            "com.android.wallpaper.livepicker",
            "com.boombuler.system.appwidgetpicker",
            "com.broadcom.bt.app.system",
            "com.demboos.galaxy.music.widget",
            "com.fede.launcher",
            "com.google.android.apps.uploader",
            "com.google.android.gms",
            "com.google.android.gsf",
            "com.google.android.location",
            "com.google.android.partnersetup",
            "com.google.android.street",
            "com.google.android.syncadapters.calendar",
            "com.google.android.syncadapters.contacts",
            "com.samsung.android.app.divx",
            "com.samsung.InputEventApp",
            "com.samsung.mobileTracker.ui",
            "com.samsung.sec.android.application.csc",
            "com.samsung.simdetachnotifier",
            "com.sec.android.app.callsetting",
            "com.sec.android.app.drmua",
            "com.sec.android.app.GpsSetup2",
            "com.sec.android.app.lcdtest",
            "com.sec.android.app.personalization",
            "com.sec.android.app.phoneutil",
            "com.sec.android.app.screencapture",
            "com.sec.android.app.servicemodeapp",
            "com.sec.android.app.shutdown",
            "com.sec.android.app.sns",
            "com.sec.android.app.snsaccount",
            "com.sec.android.app.twlauncher",
            "com.sec.android.inputmethod.axt9",
            "com.sec.android.provider.badge",
            "com.sec.android.provider.logsprovider",
            "com.sec.android.providers.downloads",
            "com.sec.android.providers.drm",
            "com.sec.app.OmaCP",
            "com.sec.app.RilErrorNotifier",
            "com.smlds",
            "com.sonyericsson.home",
            "com.svox.pico",
            "com.swype.android.inputmethod",
            "com.wipereceiver",
            "com.wsomacp",
            "gmail",
            "net.thinkindifferent.inputmethod.latin",
            "org.adw.launcher",
    };
}
