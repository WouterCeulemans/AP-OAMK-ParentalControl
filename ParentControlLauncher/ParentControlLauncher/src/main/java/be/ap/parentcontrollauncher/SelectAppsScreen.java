package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectAppsScreen extends Activity {

    private ArrayList<Item> listApp = new ArrayList<Item>();
    private ListView listView;
    private AppsAdapter AppsAdapter;
    public Pair p = new Pair();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps_screen);

        listView = (ListView) findViewById(R.id.listview1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item app = (Item) adapterView.getItemAtPosition(i);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_check);
                checkBox.setChecked(!checkBox.isChecked());
                app.visible = !app.visible;
            }
        });
        p.context = getApplicationContext();
        p.contentResolver = getContentResolver();
        p.ActivityContext = this;
        new LoadApplications().execute(p);
    }

    @Override
    public void onBackPressed() {
        Log.d("TEST", "Back Pressed");

        Applications.UpdateAppsToDB(getContentResolver(), listApp);
        super.onBackPressed();
    }

    private class LoadApplications extends AsyncTask<Pair, Void, Pair>
    {
        @Override
        protected Pair doInBackground(Pair... param)
        {
            ArrayList<Item> apps;
            apps = Applications.GetAppsFromDB(param[0].context, param[0].contentResolver, false);
            param[0].appList = apps;
            return param[0];
        }

        protected void onPostExecute(Pair result) {
            listApp = result.appList;
            AppsAdapter = new AppsAdapter(result.ActivityContext, R.layout.row_list, result.appList);
            listView.setAdapter(AppsAdapter);
        }
    }

    private class Pair
    {
        public Context context;
        public Context ActivityContext;
        public ContentResolver contentResolver;
        public ArrayList<Item> appList;
    }
}