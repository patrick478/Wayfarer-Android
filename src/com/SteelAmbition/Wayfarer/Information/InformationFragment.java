package com.SteelAmbition.Wayfarer.Information;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.SteelAmbition.Wayfarer.Dangers.DangerRowAdapter;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.Utils;
import com.SteelAmbition.Wayfarer.data.Danger;
import com.SteelAmbition.Wayfarer.data.InformCard;
import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class InformationFragment extends SherlockFragment {


	private ArrayList<InformCard> arrayOfList;
    private InformationRowAdapter objAdapter;
	private ListView informationList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.informations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        informationList = (ListView)getView().findViewById(R.id.informationList);
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


            arrayOfList = new ArrayList<InformCard>();

            if (null == result || result.length() == 0) {
                //TODO: handle no data found
            } else {



                for (InformCard ic : MainActivity.stateManager.getInformCards()) {
                    arrayOfList.add(ic);
                }




                setAdapterToListview();

            }
           getSherlockActivity().setProgressBarIndeterminateVisibility(false);
        }
    }

    public void setAdapterToListview() {
        objAdapter = new InformationRowAdapter(getSherlockActivity(), arrayOfList);
        informationList.setAdapter(objAdapter);
        getSherlockActivity().setProgressBarIndeterminateVisibility(false);
    }


}
