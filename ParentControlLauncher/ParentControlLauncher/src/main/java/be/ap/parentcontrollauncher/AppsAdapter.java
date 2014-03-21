package be.ap.parentcontrollauncher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import be.ap.parentcontrollauncher.Item;
import be.ap.parentcontrollauncher.R;

/**
 * Created by Wouter on 8/02/14.
 */
public class AppsAdapter extends ArrayAdapter<Item> {

    private Context myContext;
    private int layoutResourceId;
    private ArrayList<Item> MyAppsList;

    public AppsAdapter(Context c, int layoutResourceId, ArrayList<Item> appList) {
        super(c, layoutResourceId, appList);
        this.myContext = c;
        this.MyAppsList = appList;
        this.layoutResourceId = layoutResourceId;
    }

   /* @Override
    public int getCount() {
        return MyAppsList.size();
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            if (layoutResourceId == R.layout.row_list) {
                holder.checkItem = (CheckBox) row.findViewById(R.id.item_check);
            }
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        Item item = MyAppsList.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imageItem.setImageBitmap((item.getImage()));
        if (layoutResourceId == R.layout.row_list) {
            holder.checkItem.setChecked(item.visible);
        }
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
        CheckBox checkItem;
    }
}