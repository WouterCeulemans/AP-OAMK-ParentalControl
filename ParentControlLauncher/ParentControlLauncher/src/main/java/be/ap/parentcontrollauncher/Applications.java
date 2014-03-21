package be.ap.parentcontrollauncher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wouter on 21/02/14.
 */
public class Applications {

    public static ArrayList<Item> GetInstalledApps(Context context)
    {
        ArrayList<Item> appList = new ArrayList<Item>();
        List<ApplicationInfo> myApplications;

        final PackageManager pm = context.getPackageManager();
        myApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : myApplications)
        {
            appList.add(new Item(((BitmapDrawable) packageInfo.loadIcon(pm)).getBitmap(), (String) pm.getApplicationLabel(packageInfo), packageInfo.packageName));
        }
        return appList;
    }

    public static ArrayList<Item> GetSelectedApps (Context context)
    {
        ArrayList<Item> appList = new ArrayList<Item>();
        List<ApplicationInfo> myApplications;
        final PackageManager pm = context.getPackageManager();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(context.getFilesDir(), "app_list.txt")));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] itemInfo = line.split(",");
                ApplicationInfo appInfo = pm.getApplicationInfo(itemInfo[0].toString(), PackageManager.GET_META_DATA);
                appList.add(new Item(((BitmapDrawable)appInfo.loadIcon(pm)).getBitmap(), (String)pm.getApplicationLabel(appInfo), appInfo.packageName));
            }
            Log.d("File", "File read successfully");
        }
        catch (Exception e) {
        }
        finally {
            return appList;
        }
    }

    public static void SaveVisibleApps(Context context, ArrayList<Item> appList)
    {
        File appListFile = new File(context.getFilesDir(), "app_list.txt");
        if (appListFile.exists())
            appListFile.delete();

        try {
                appListFile.createNewFile();
                FileWriter writer = new FileWriter(appListFile);

                try {
                    for (Item item : appList) {
                        writer.write(item.toString() + System.getProperty("line.separator"));
                    }
                    Log.d("FILE", "File is saved");
                }
                finally {
                    writer.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static AppsAdapter appsAdapter;
    public static ArrayList<Item> appList;
}
