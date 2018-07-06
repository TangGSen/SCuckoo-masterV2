package imagepicker.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by Sen on 2016/1/18.
 */
public class ResourcesUtils {
   static Resources resources=null;
    public static Resources getResources(Context context){
        if (resources==null) {
            resources = context.getResources();
        }
        return resources;

    }

    public static String getResString(Context context,int id){
       return getResources(context).getString(id);
    }

    public static Drawable getResDrawable(Context context,int id){
        return getResources(context).getDrawable(id);
    }

    public static int getResColor(Context context,int id){
        return getResources(context).getColor(id);
    }

    public static String[] getStringArray(Context context,int id) {
        return getResources(context).getStringArray(id);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = getResources(context).getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources(context).getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
