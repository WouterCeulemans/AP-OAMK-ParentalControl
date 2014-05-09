package be.ap.parentcontrollauncher;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Wouter on 9/05/2014.
 */
public class SendDataService extends IntentService {

    private Context mContext;
    private JSONHandler jsonHandler;
    private NetClient netClient;
    public SendDataService()
    {
        super("SendDataService");
        mContext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        jsonHandler = new JSONHandler(mContext);
        netClient = new NetClient("81.83.164.27", 8041);
        netClient.ConnectWithServer();
        for (String json : jsonHandler.Serialize()) {
            netClient.SendDataToServer("push;" + json);
        }
        netClient.DisConnectWithServer();
    }
}
