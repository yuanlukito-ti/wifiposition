package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

import android.app.Application;
import android.content.Context;

/**
 * Created by Yuan Lukito on 3/24/2015.
 */
public class ApplicationContextProvider extends Application {
    private static Context sContext;

    public void onCreate(){
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext(){
        return sContext;
    }
}
