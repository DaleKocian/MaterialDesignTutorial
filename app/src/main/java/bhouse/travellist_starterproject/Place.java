package bhouse.travellist_starterproject;

import android.content.Context;

public class Place {

    public static final String DEF_TYPE = "drawable";
    public String name;
    public String imageName;
    public boolean isFav;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, DEF_TYPE, context.getPackageName());
    }
}
