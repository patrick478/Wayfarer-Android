package com.SteelAmbition.Wayfarer.Goals;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.Utils;
import com.SteelAmbition.Wayfarer.data.Goal;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.espiandev.showcaseview.ShowcaseView;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalsFragment extends SherlockFragment {


	private ArrayList<Goal> arrayOfList;
    private GoalRowAdapter objAdapter;
	private ListView goalList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.goals, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        goalList = (ListView)getView().findViewById(R.id.goalsList);
        if(MainActivity.stateManager!=null)
            new ShowGoals().execute();
    }





    class ShowGoals extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getSherlockActivity().setProgressBarIndeterminateVisibility(true);


        }

        @Override
        protected String doInBackground(String... params) {
            return Utils.getJSONString("http://wayfarer-server.herokuapp.com/steps");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            arrayOfList = new ArrayList<Goal>();

            if (null == result || result.length() == 0) {
                //TODO: handle no data found
            } else {



                for (Goal g : MainActivity.stateManager.getPreventionGoals()) {
                    arrayOfList.add(g);
                }

                for (Goal g : MainActivity.stateManager.getLongTermGoals()) {
                    arrayOfList.add(g);
                }

                for (Goal g : MainActivity.stateManager.getRegularGoals()) {
                    arrayOfList.add(g);
                }



                setAdapterToListview();

            }
           getSherlockActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    public void setAdapterToListview() {
        objAdapter = new GoalRowAdapter(getSherlockActivity(), arrayOfList);
        goalList.setAdapter(objAdapter);
        getSherlockActivity().setProgressBarIndeterminateVisibility(false);
    }


}
