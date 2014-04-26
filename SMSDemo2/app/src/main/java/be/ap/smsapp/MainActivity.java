package be.ap.smsapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
{
    /**called when the activity first created*/
    Button sendSMS;
    EditText msgTxt;
    EditText numTxt;
    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // display the message in the textview
            TextView inTxt = (TextView) findViewById(R.id.textMsg);
            inTxt.setText(intent.getExtras().getString("sms"));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intent to filter for SMS messages
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");


        sendSMS = (Button) findViewById(R.id.sendBtn);
        msgTxt =(EditText) findViewById(R.id.MessageTxt);
        numTxt = (EditText) findViewById(R.id.PhoneTxt);
        sendSMS.setOnClickListener
                (
                        new View.OnClickListener()
                            {
                                @Override
                        public void onClick(View v)
                                {
                                    String myMsg        = msgTxt.getText().toString();
                                    String theNumber    = numTxt.getText().toString();
                                    sendMsg(theNumber, myMsg);
                                }
                            }
                );


    }

    protected Boolean CheckSmsAvailable(String number)
    {
        Integer TxtAmount = 0;
        Integer TxtMax = 0;
        String selection = Database.COLUMN_PHONENUMBER + " = " + number;
        Cursor cur = this.getContentResolver().query(Database.CONTENT_URI_CONTACTS, null, selection, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                TxtAmount = cur.getInt(cur.getColumnIndex(Database.COLUMN_TXTAMOUNT));
                TxtMax = cur.getInt(cur.getColumnIndex(Database.COLUMN_TXTMAX));
            }
            cur.close();
        }
            if (TxtAmount < TxtMax)
                //txtamount verhogen en updaten
                return true;
            else
                return false;
    }

    protected void sendMsg(String theNumber, String myMsg)
    {
        String SENT         = "Message Sent";
        String DELIVERED    = "Message Delivered";

        PendingIntent sentPI        =   PendingIntent.getBroadcast(this, 0  , new Intent(SENT)      , 0);
        PendingIntent deliveredPI   =   PendingIntent.getBroadcast(this, 0  , new Intent(DELIVERED) , 0);

        registerReceiver(new BroadcastReceiver()
        {
            public void onReceive(Context arg0, Intent arg1)
            {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),"No Service", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), " SMS not delivered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent);
        sms.sendTextMessage(theNumber, null , myMsg, sentPI, deliveredPI);
    }
    @Override
    protected void onResume()
    {
        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        //unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
