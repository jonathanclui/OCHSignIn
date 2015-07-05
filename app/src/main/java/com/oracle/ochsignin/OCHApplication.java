package com.oracle.ochsignin;

import android.content.Context;
import android.content.SharedPreferences;

import com.oracle.ochsignin.models.OCHEmployee;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by jonathanlui on 7/4/15.
 */
public class OCHApplication extends android.app.Application {
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "OCHSignInApp";

    public static String APP_ID;
    public static String CLIENT_KEY;

    private static SharedPreferences preferences;

    public OCHApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore
        APP_ID = getString(R.string.parse_app_id);
        CLIENT_KEY = getString(R.string.parse_client_key);

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(OCHEmployee.class);
        Parse.initialize(this, APP_ID, CLIENT_KEY);

        preferences = getSharedPreferences("com.oracle.ochsignin", Context.MODE_PRIVATE);
    }
}

