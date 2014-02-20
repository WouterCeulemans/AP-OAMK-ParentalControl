package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        menu.add(0, 11, 0, "Apps")
                .setIcon(android.R.drawable.ic_menu_gallery);
        menu.add(0, 12, 0, "SMS")
                .setIcon(android.R.drawable.ic_menu_send);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle item selection
        switch (item.getItemId()){
            case 11:
                showAppsPage();
            case 12:
                showSmsApp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAppsPage(){
        //Not yet implemented

        //Intent showApps = new Intent(this, DisplayAppsScreen.class);
        //startActivity(showApps);
    }

    private void showSmsApp()
    {
        //Not yet implemented

        //Intent smsApp = new Intent(this, SmsApp.class);
        //startActivity(smsApp);
    }
}
