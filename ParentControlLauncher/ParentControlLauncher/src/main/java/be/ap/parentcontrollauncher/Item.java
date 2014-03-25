package be.ap.parentcontrollauncher;

import android.graphics.Bitmap;

/**
 * Created by Wouter on 9/02/14.
 */
public class Item {
    Bitmap image;
    String title;
    public String packageName;
    boolean visible;
    int ID;


    public Item(Bitmap image, String title, String packageName, boolean visible, int id) {
        super();
        this.image = image;
        this.title = title;
        this.packageName = packageName;
        this.visible = visible;
        this.ID = id;
    }

    public Item(Bitmap image, String title, String packageName, boolean visible) {
        this(image, title, packageName, visible, -1);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return packageName + "," + visible;
    }
}