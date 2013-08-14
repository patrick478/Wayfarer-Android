package com.SteelAmbition.Wayfarer;

import android.app.Activity;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

public class MainActivity extends SherlockActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        setContentView(R.layout.main);
    }
}
