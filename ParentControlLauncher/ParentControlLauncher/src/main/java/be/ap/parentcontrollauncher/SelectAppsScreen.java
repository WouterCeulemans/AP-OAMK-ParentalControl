package be.ap.parentcontrollauncher;

import android.app.Activity;
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
                app.checked = !app.checked;
            }
        });
        new LoadApplications().execute(this);
    }

    @Override
    public void onBackPressed() {
        Log.d("TEST", "Back Pressed");
        Iterator<Item> i = listApp.iterator();

            try {
                while (i.hasNext())
                {
                    Item app = i.next();
                    if (!app.checked)
                    {
                        i.remove();
                    }
                }
                //int i = 0;
                /*for (Item app : listApp) {
                    //v = listView.getAdapter().getView(i, null, null);
                    //checkBox = (CheckBox) v.findViewById(R.id.item_check);
                    if (!app.checked)
                    {
                        listApp.remove(app);
                    }
                    //i++;
                }*/

                Applications.SaveVisibleApps(this, listApp);
            }
            catch (Exception e) {

            }
        //Log.d("TEST", "File Saved");
        super.onBackPressed();
    }

    private class LoadApplications extends AsyncTask<Context, Void, Pair>
    {
        @Override
        protected Pair doInBackground(Context... param)
        {
            ArrayList<Item> apps;
            Pair p = new Pair();
            p.context = param[0];
            apps = Applications.GetInstalledApps(param[0]);

            File appListFile = new File(getFilesDir(), "app_list.txt");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(appListFile));
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] itemInfo = line.split(",");
                    for (int i = 0; i < apps.size(); i++) {
                        //Log.d("FileName", "List: " + listArray.get(i).packageName);
                        //Log.d("FileName", "Item: " + itemInfo[0]);
                        if (apps.get(i).packageName.equals(itemInfo[0]))
                        {
                            apps.get(i).checked = true;
                            //Log.d("Check", "App is checked");
                            continue;
                        }
                    /*else
                    {
                        listArray.get(i).checked = false;
                    }*/
                    }
                }
                Log.d("File", "File read successfully");
                p.appList = apps;
            }
            catch (Exception e) {
                Log.d("Error", "Exception occured: " + e.getMessage());
            }
            return p;
        }

        protected void onPostExecute(Pair result) {
            listApp = result.appList;
            AppsAdapter = new AppsAdapter(result.context, R.layout.row_list, result.appList);
            listView.setAdapter(AppsAdapter);
        }
    }

    private class Pair
    {
        public Context context;
        public ArrayList<Item> appList;
    }
}