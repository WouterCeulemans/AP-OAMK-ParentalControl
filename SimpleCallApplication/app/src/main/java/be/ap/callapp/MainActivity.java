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
    import android.widget.EditText;

public class MainActivity extends Activity {

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
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                    phoneCallIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString().trim()));
                    startActivity(phoneCallIntent);
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

	}

