package be.ap.smsapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity
{
    /**called when the activity first created*/
    ImageButton sendSMS;
    EditText msgTxt;
    EditText numTxt;
    IntentFilter intentFilter;
    SmsManager sm = SmsManager.getDefault();

    Integer id = null;
    Integer Textamount;

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

        TEST_CheckNumberExists();


        //intent to filter for SMS messages
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");


        sendSMS = (ImageButton) findViewById(R.id.sendBtn);
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
                                    if (CheckSmsAvailable(theNumber)) {
                                        if (myMsg.length() < 160)
                                            sendMsg(theNumber, myMsg);
                                        else
                                            sendMultipartMsg(theNumber, myMsg);
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Limit reached or number blocked", Toast.LENGTH_SHORT).show();
                                }
                            }
                );


    }

    protected Boolean CheckSmsAvailable(String number)
    {
        Integer TxtAmount = 0;
        Integer TxtMax = 0;
        Integer Blocked = 0;
        Integer id = 1;
        Cursor cur = this.getContentResolver().query(Database.CONTENT_URI_CONTACTS, null, Database.COLUMN_PHONENUMBER + " = ?", new String[] {number}, null);
        //Cursor cur = this.getContentResolver().query(Database.CONTENT_URI_CONTACTS, null, Database.COLUMN_ID + " = ?", new String[] {"1"}, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                id = cur.getInt(cur.getColumnIndex(Database.COLUMN_ID));
                TxtAmount = cur.getInt(cur.getColumnIndex(Database.COLUMN_TXTAMOUNT));
                TxtMax = cur.getInt(cur.getColumnIndex(Database.COLUMN_TXTMAX));
                Blocked = cur.getInt(cur.getColumnIndex(Database.COLUMN_BLOCKED));
            }
            cur.close();
        }
        if (IntToBoolean(Blocked)) {
            if (TxtAmount < TxtMax) {
                TxtAmount++;
                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_TXTAMOUNT, TxtAmount);
                getContentResolver().update(Database.CONTENT_URI_CONTACTS, values, Database.COLUMN_ID + " = ?", new String[] {id.toString()});
                return true;
            } else
                return false;
        } else
            return false;
    }

    private Boolean IntToBoolean(Integer integer)
    {
        if (integer <= 0)
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

    private void sendMultipartMsg(String theNumber, String myMsg)
    {
        String SENT         = "Message Sent";
        String DELIVERED    = "Message Delivered";

        PendingIntent sentPI        =   PendingIntent.getBroadcast(this, 0  , new Intent(SENT)      , 0);
        PendingIntent deliveredPI   =   PendingIntent.getBroadcast(this, 0  , new Intent(DELIVERED) , 0);


        ArrayList<String> parts = sm.divideMessage(myMsg);
        int numParts = parts.size();

        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

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

        for (int i = 0; i < numParts; i++) {
            sentIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0));
            deliveryIntents.add(PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0));
        }

        sm.sendMultipartTextMessage(theNumber, null, parts, sentIntents, deliveryIntents);
    }


    private void TEST_CheckNumberExists()
    {
        try {

            Cursor cur = this.getContentResolver().query(Database.CONTENT_URI_CONTACTS, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    Toast.makeText(getApplicationContext(), "Beschikbaar", Toast.LENGTH_SHORT);
                    id = cur.getInt(cur.getColumnIndex(Database.COLUMN_ID));
                    Textamount = cur.getInt(cur.getColumnIndex(Database.COLUMN_TXTAMOUNT));
                }
            }
            cur.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
