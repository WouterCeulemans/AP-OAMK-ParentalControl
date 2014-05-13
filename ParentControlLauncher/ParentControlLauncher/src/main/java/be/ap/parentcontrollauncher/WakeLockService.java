package be.ap.parentcontrollauncher;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Calendar;

import be.ap.parentcontrollauncher.contentprovider.ParentControlContentProvider;
import be.ap.parentcontrollauncher.database.LocationsTable;

/**
 * Created by Wouter on 27/03/2014.
 */
public class WakeLockService extends IntentService {

    private final Context mContext;

    public WakeLockService() {
        super("WakeLockService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        /*if (hour == LockTime.FromHour)
            LockTime.Locked = true;
        else if (hour == LockTime.ToHour)
            LockTime.Locked = false;*/

        if (minutes == LockTime.FromMinutes)
            LockTime.Locked = true;
        else if (minutes == LockTime.ToMinutes)
            LockTime.Locked = false;
    }
}
