package Tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.R.id;
import com.SteelAmbition.Wayfarer.R.layout;
import com.actionbarsherlock.app.SherlockFragment;


/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 23/06/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TasksFragment extends SherlockFragment {


	private ArrayList<Task> arrayOfList;
    private TaskRowAdapter objAdapter;
	private ListView taskList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
         taskList = (ListView)getView().findViewById(R.id.tasksList);
         new ShowTasks().execute();
    }





    class ShowTasks extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            return com.SteelAmbition.Wayfarer.Utils.getJSONString("http://wayfarer-server.herokuapp.com/getTest");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            getSherlockActivity().setProgressBarIndeterminateVisibility(false);


            if (null == result || result.length() == 0) {
                //TODO: handle no data found
            } else {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objJson = jsonArray.getJSONObject(i);


                            Task task = new Task();


                            task.description = objJson.getString("title");
                            task.title = (objJson.getString("description"));


                            arrayOfList.add(task);
                        }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterToListview();

            }

        }
    }

    public void setAdapterToListview() {
        objAdapter = new TaskRowAdapter(getSherlockActivity(),
                arrayOfList);
        taskList.setAdapter(objAdapter);
        getSherlockActivity().setProgressBarIndeterminateVisibility(false);
    }


}
