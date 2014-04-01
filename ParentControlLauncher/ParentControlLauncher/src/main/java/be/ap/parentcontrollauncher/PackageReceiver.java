package be.ap.parentcontrollauncher;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.ApplicationsTable;

/**
 * Created by Wouter on 26/03/2014.
 */
public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent)
    {
        String data = intent.getData().toString();
        data = data.replace("package:", "");
        String action = intent.getAction();
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);

        if (!replacing)
        {
            if (action == "android.intent.action.PACKAGE_ADDED")
            {
                PackageManager pm = context.getPackageManager();
                String title = "";
                try
                {
                    ApplicationInfo appInfo = pm.getApplicationInfo(data, PackageManager.GET_META_DATA);
                    title = (String) pm.getApplicationLabel(appInfo);
                }
                catch (Exception e)
                {
                    Log.d("Apps", e.getMessage());
                    Log.d("Apps", "Error while getting app from DB: " + data);
                }

                ContentValues values;
                values = new ContentValues();
                values.put(ApplicationsTable.COLUMN_TITLE, title);
                values.put(ApplicationsTable.COLUMN_PACKAGE, data);
                values.put(ApplicationsTable.COLUMN_VISIBLE, true);
                context.getContentResolver().insert(ParentControlContentProvider.CONTENT_URI_APPS, values);

                Log.d("receiver", "App added to DB");

                Applications.appList = Applications.GetAppsFromDB(context, context.getContentResolver(), true);
            }
            else if (action == "android.intent.action.PACKAGE_REMOVED")
            {
                try {

                    String selection = ApplicationsTable.COLUMN_PACKAGE + " = '" + data + "'";
                    context.getContentResolver().delete(ParentControlContentProvider.CONTENT_URI_APPS, selection, null);
                    Log.d("receiver", "App removed from DB");
                }
                catch (Exception e)
                {
                    Log.d("receiver", e.getMessage());
                }
            }
        }
    }
}
