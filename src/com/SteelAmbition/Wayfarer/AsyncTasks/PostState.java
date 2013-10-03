package com.SteelAmbition.Wayfarer.AsyncTasks;

import android.os.AsyncTask;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.data.StateManager;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 1/10/13
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class PostState extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        StateManager.postState(MainActivity.stateManager, MainActivity.subjectID);
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
    }
}
