package be.ap.smsapp;

/**
 * Created by nick on 19/02/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

public class SMSReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //get message passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str = "" ;
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++)
            {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str +=  messages[i].getOriginatingAddress() + " Says" ;
                str += " : ";
                str += messages[i].getMessageBody().toString();
                str += " \n " ;
            }
            //display message
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            // send a broadcast intent to update the SMSreceived in a textview
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
