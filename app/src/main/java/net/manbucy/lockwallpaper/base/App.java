package net.manbucy.lockwallpaper.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.stericson.RootTools.RootTools;

/**
 * App
 * Created by yang on 2017/7/6.
 */

public class App extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("---APP", "onCreate: ");
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
