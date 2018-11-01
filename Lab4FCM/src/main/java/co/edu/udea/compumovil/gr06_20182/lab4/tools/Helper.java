package co.edu.udea.compumovil.gr06_20182.lab4.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class Helper {
    public static Integer getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getInt(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", "Unable to load meta-data: " + e.getMessage());
            return 60000;
        }
    }

}
