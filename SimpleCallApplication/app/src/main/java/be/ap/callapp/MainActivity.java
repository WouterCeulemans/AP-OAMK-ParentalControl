package be.ap.callapp;

/* ik zie het verschil niet met mijn code , "grijs" werkt niet , de internet source code , werkt wel.

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity
{
    final Context context = this;
    private Button btn;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);

        PhoneCallListener phoneCallListener = new PhoneCallListener();
        TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

        btn.setOnClickListener(new OnClickListener()
        {
           @Override
        public void onClick(View view)
           {
               Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
               phoneCallIntent.setData(Uri.parse("tel:123456789"));
               startActivity(phoneCallIntent);
           }
        });
    }

    // monitor phonecall states
    private class PhoneCallListenrer extends PhoneStateListener
    {
        String TAG = " LOGGING PHONE CALL";

        private boolean phoneCalling = false;

        @Override
        public void onCallStateChanged(int state , String incimingNumber)
        {
            if (TelephonyManager.CALL_STATE_RINGING == state)
            {//Phone ringing
                Log.i(TAG, " RINGING , number : " + incomingNumber);
            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state)
            {//active
                Log.i(TAG, " OFFHOOK");

                phoneCalling =  true;
            }

            //when the call ends launch the main activity again
            if (TelephonyManager.CALL_STATE_IDLE == state)
            {
                Log.i(TAG, "IDLE");

                if (phoneCalling)
                {
                    Log.i(TAG, " Restart the app");
                    //restart app
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    phoneCalling = false;
                }
            }
        }
    }
}
*/
//____________________________________________________________________


	import android.app.Activity;
    import android.content.ContentValues;
    import android.content.Context;
	import android.content.Intent;
    import android.database.Cursor;
    import android.net.Uri;
	import android.os.Bundle;
    import android.provider.CallLog;
    import android.telephony.PhoneStateListener;
	import android.telephony.TelephonyManager;
	import android.util.Log;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.Button;
    import android.widget.EditText;

public class MainActivity extends Activity {

        Integer CallAmount;
	    final Context context = this;
	    private Button btn;

	    public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        btn = (Button) findViewById(R.id.button);
            Button callButton = (Button)findViewById(R.id.CallButton);
            final EditText phoneNumber = (EditText)findViewById(R.id.PhoneNumberTxt);

	        PhoneCallListener phoneCallListener = new PhoneCallListener();
	        TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	        telManager.listen(phoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

	        // add button listener
	        btn.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View view) {

	                Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
	                phoneCallIntent.setData(Uri.parse("tel:123456"));
	                startActivity(phoneCallIntent);

	            }

	        });

            callButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CheckCallAvailable(phoneNumber.getText().toString().trim())) {
                        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                        phoneCallIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString().trim()));
                        startActivity(phoneCallIntent);
                    }
                }
            });

	    }

	    // monitor phone call states
	    private class PhoneCallListener extends PhoneStateListener {

	        String TAG = "LOGGING PHONE CALL";

	        private boolean phoneCalling = false;

	        @Override
	        public void onCallStateChanged(int state, String incomingNumber) {

	            if (TelephonyManager.CALL_STATE_RINGING == state) {
	                // phone ringing
	                Log.i(TAG, "RINGING, number: " + incomingNumber);
	            }

	            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
	                // active
	                Log.i(TAG, "OFFHOOK");

	                phoneCalling = true;
	            }

	            // When the call ends launch the main activity again
	            if (TelephonyManager.CALL_STATE_IDLE == state) {

	                Log.i(TAG, "IDLE");

	                if (phoneCalling) {

                        CallAmount += getDuration(incomingNumber.toString().trim());
                        ContentValues values = new ContentValues();
                        values.put(Database.COLUMN_CALLAMOUNT, CallAmount);
                        String selection = Database.COLUMN_PHONENUMBER + " = " + incomingNumber.toString().trim();
                        getContentResolver().update(Database.CONTENT_URI_CONTACTS, values, selection, null);
	                    Log.i(TAG, "restart app");

	                    // restart app
	                    Intent i = getBaseContext().getPackageManager()
	                            .getLaunchIntentForPackage(
	                                    getBaseContext().getPackageName());

	                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(i);

	                    phoneCalling = false;
	                }

	            }
	        }
	    }

    protected Boolean CheckCallAvailable(String number)
    {
        CallAmount = 0;
        Integer CallMax = 0;
        Integer Blocked = 0;
        String selection = Database.COLUMN_PHONENUMBER + " = " + number;
        Cursor cur = this.getContentResolver().query(Database.CONTENT_URI_CONTACTS, null, selection, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                CallAmount = cur.getInt(cur.getColumnIndex(Database.COLUMN_CALLAMOUNT));
                CallMax = cur.getInt(cur.getColumnIndex(Database.COLUMN_CALLMAX));
                Blocked = cur.getInt(cur.getColumnIndex(Database.COLUMN_BLOCKED));
            }
            cur.close();
        }
        if (IntToBoolean(Blocked)) {
            if (CallAmount < CallMax) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    private Boolean IntToBoolean(Integer integer)
    {
        if (integer <= 0)
            return false;
        else
            return true;
    }

    private Integer getDuration(String number)
    {
        Integer duration;
        String selection = CallLog.Calls.NUMBER + " = '" + number + "'";
        Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, selection, null, null);
        if (cur.moveToFirst())
        {
            duration = cur.getColumnIndex(CallLog.Calls.DURATION);
            cur.close();
            return duration;
        }
        else {
            duration = 0;
            cur.close();
            return duration;
        }
    }
}

