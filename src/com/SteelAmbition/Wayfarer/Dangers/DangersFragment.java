package com.SteelAmbition.Wayfarer.Dangers;

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
import com.SteelAmbition.Wayfarer.data.Danger;
import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DangersFragment extends SherlockFragment {


	private ArrayList<Danger> arrayOfList;
    private DangerRowAdapter objAdapter;
	private ListView dangersList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dangers, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dangersList = (ListView)getView().findViewById(R.id.dangersList);
        if(MainActivity.stateManager!=null)
            new ShowDangers().execute();
    }





    class ShowDangers extends AsyncTask<String, Void, String> {

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


            arrayOfList = new ArrayList<Danger>();

            if (null == result || result.length() == 0) {
                //TODO: handle no data found
            } else {



                for (Danger d : MainActivity.stateManager.getDangers()) {
                    arrayOfList.add(d);
                }




                setAdapterToListview();

            }
           getSherlockActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    public void setAdapterToListview() {
        objAdapter = new DangerRowAdapter(getSherlockActivity(), arrayOfList);
        dangersList.setAdapter(objAdapter);
        getSherlockActivity().setProgressBarIndeterminateVisibility(false);
    }


}
