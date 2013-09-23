package com.SteelAmbition.Wayfarer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TabListener implements ActionBar.TabListener {

    private final FragmentActivity mActivity;
    private final String mTag;
    private final Class mFragmentClass;
    private Fragment mFragment;

    public TabListener(FragmentActivity activity, String tag, Class fragmentClass) {
        mActivity = activity;
        mTag = tag;
        mFragmentClass = fragmentClass;
        mFragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment == null) {
            mFragment = SherlockFragment.instantiate(mActivity, mFragmentClass.getName());
            // place in the default root viewgroup - android.R.id.content
            ft.replace(android.R.id.content, mFragment, mTag);

        } else {
            if(mFragment.isDetached())
                ft.attach(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null){
            ft.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
