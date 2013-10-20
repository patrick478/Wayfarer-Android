package com.SteelAmbition.Wayfarer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.Network.ServerAccess;
import com.SteelAmbition.Wayfarer.data.Goal;
import com.SteelAmbition.Wayfarer.data.StateAccess;
import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardFragment extends SherlockFragment {

    private TextView goalProgress;
    private TextView goalsCount;
    private TextView name;
    private ProgressBar goalsProgressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        goalsCount =  (TextView)getView().findViewById(R.id.goalsCount);

        goalsProgressBar =  (ProgressBar)getView().findViewById(R.id.goalsProgress);

        name = (TextView) getView().findViewById(R.id.nameHello);

        if(MainActivity.stateManager!=null)
            new ShowStats().execute();
    }

    class ShowStats extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSherlockActivity().setProgressBarIndeterminateVisibility(true);


        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            int completedGoals = MainActivity.stateManager.getCompletedGoals().size();
            int totalGoals = MainActivity.stateManager.getPreventionGoals().size() + MainActivity.stateManager.getLongTermGoals().size();

            goalsCount.setText(String.format("%s/%s", String.valueOf(completedGoals),  String.valueOf(totalGoals)));
            goalsCount.setVisibility(View.VISIBLE);

            goalsProgressBar.setMax(totalGoals);
            goalsProgressBar.setProgress(completedGoals);


            name.setText(String.format("Hello %s, welcome back!", ServerAccess.getCurrentUser().getName()));


            getSherlockActivity().setProgressBarIndeterminateVisibility(false);
}
}

}
