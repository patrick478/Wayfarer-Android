package com.SteelAmbition.Wayfarer;


import android.app.Activity;
import android.os.Bundle;
import com.SteelAmbition.Wayfarer.Tasks.TasksFragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.crashlytics.android.Crashlytics;


public class MainActivity extends SherlockFragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        Crashlytics.start(this);

        //setContentView(R.layout.main);
        addTabs();
    }


    private void addTabs() {
        com.actionbarsherlock.app.ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_TABS);

        String dashboardTitle =getResources().getString(R.string.dashboard);
        com.actionbarsherlock.app.ActionBar.Tab dashboardTab = bar.newTab().setText(dashboardTitle);
        dashboardTab.setTabListener(new TabListener(this, dashboardTitle, DashboardFragment.class));
        bar.addTab(dashboardTab);

        String tasksTitle = getResources().getString(R.string.tasks);
        com.actionbarsherlock.app.ActionBar.Tab tasksTab = bar.newTab().setText(tasksTitle);
        tasksTab.setTabListener(new TabListener(this, tasksTitle, TasksFragment.class));
        bar.addTab(tasksTab);

        String socialTitle =getResources().getString(R.string.social);
        com.actionbarsherlock.app.ActionBar.Tab socialTab = bar.newTab().setText(socialTitle);
        socialTab.setTabListener(new TabListener(this, socialTitle, SocialFragment.class));
        bar.addTab(socialTab);

    }
}
