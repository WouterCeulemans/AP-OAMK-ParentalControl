package be.ap.parentcontrollauncher;

import android.app.Activity;
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

public class SelectAppsScreen extends Activity {

    private ArrayList<Item> appList = new ArrayList<Item>();
    private ListView listView;
    private AppsAdapter AppsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps_screen);

        appList = Applications.GetInstalledApps(this);

        File appListFile = new File(getFilesDir(), "app_list.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(appListFile));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] itemInfo = line.split(",");
                for (int i = 0; i < appList.size(); i++) {
                    //Log.d("FileName", "List: " + listArray.get(i).packageName);
                    //Log.d("FileName", "Item: " + itemInfo[0]);
                    if (appList.get(i).packageName.equals(itemInfo[0]))
                    {
                        appList.get(i).checked = true;
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
        }
        catch (Exception e) {
            Log.d("Error", "Exception occured: " + e.getMessage());
        }

        listView = (ListView) findViewById(R.id.listview1);
        AppsAdapter = new AppsAdapter(this, R.layout.row_list, appList);
        listView.setAdapter(AppsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item app = (Item) adapterView.getItemAtPosition(i);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_check);
                checkBox.setChecked(!checkBox.isChecked());
                app.checked = !app.checked;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("TEST", "Back Pressed");
            View v;
            CheckBox checkBox;

            try {

                for (int i = 0; i < appList.size(); i++) {
                    v = listView.getAdapter().getView(i, null, null);
                    checkBox = (CheckBox) v.findViewById(R.id.item_check);
                    if (!checkBox.isChecked())
                    {
                        for (Item item : appList)
                        {
                            if (item.getTitle() == ((TextView)v.findViewById(R.id.item_text)).getText())
                            {
                                appList.remove(item);
                                break;
                            }
                        }
                    }
                }

                Applications.SaveVisibleApps(this, appList);
            }
            catch (Exception e) {

            }
        //Log.d("TEST", "File Saved");
        super.onBackPressed();
    }
}
