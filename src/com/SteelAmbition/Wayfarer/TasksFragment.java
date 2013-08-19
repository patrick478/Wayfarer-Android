package com.SteelAmbition.Wayfarer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TasksFragment extends SherlockFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView taskList = (ListView)getView().findViewById(R.id.tasksList);
    }
}
