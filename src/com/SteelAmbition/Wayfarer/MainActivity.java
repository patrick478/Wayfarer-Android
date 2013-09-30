package com.SteelAmbition.Wayfarer;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import com.SteelAmbition.Wayfarer.Goals.GoalsFragment;
import com.SteelAmbition.Wayfarer.crouton.Crouton;
import com.SteelAmbition.Wayfarer.crouton.Style;
import com.SteelAmbition.Wayfarer.data.StateManager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockFragmentActivity {
    public static StateManager stateManager;
    public static String userID;
    public static String subjectID;

    private Activity activity;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Crashlytics.start(this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        ///load the file
        userID = "52073447a8f0a70200000001";
        subjectID  = "524110645924220200000007";

        stateManager = null;

        activity = this;

        //setContentView(R.layout.main);
        //stateManager = Main.readState(subjectID);
        setUp();
        addTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.emergency_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO fix up
//        switch (item.getItemId()) {
//            case R.menu.emergency_menu:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                //0800543354 is the real number
                callIntent.setData(Uri.parse("tel:02108210330"));
                startActivity(callIntent);
                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }
    @Override
    public void onStop(){
        super.onStop();
        //TODO commit changes

        new PostState().execute();
    }


    private void addTabs() {
        com.actionbarsherlock.app.ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(com.actionbarsherlock.app.ActionBar.NAVIGATION_MODE_TABS);

        String goalsTitle = getResources().getString(R.string.goals);
        com.actionbarsherlock.app.ActionBar.Tab goalsTab = bar.newTab().setText(goalsTitle);
        goalsTab.setTabListener(new TabListener(this, goalsTitle, GoalsFragment.class));
        bar.addTab(goalsTab);

        String dashboardTitle = getResources().getString(R.string.dashboard);
        com.actionbarsherlock.app.ActionBar.Tab dashboardTab = bar.newTab().setText(dashboardTitle);
        dashboardTab.setTabListener(new TabListener(this, dashboardTitle, DashboardFragment.class));
        bar.addTab(dashboardTab);

        String socialTitle = getResources().getString(R.string.social);
        com.actionbarsherlock.app.ActionBar.Tab socialTab = bar.newTab().setText(socialTitle);
        socialTab.setTabListener(new TabListener(this, socialTitle, SocialFragment.class));
        bar.addTab(socialTab);

    }

    private void setUp(){
//        Database db = Main.newUserDatabase("asd");
//        Survey s = new Survey(new ArrayList<Question>());
//        stateManager = new StateManager(db, s);
//        userID = db.getId();
//        Main.postState(stateManager, userID);
        new SubjectLoader().execute(subjectID);
    }

    class SubjectLoader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            MainActivity.stateManager = StateManager.readState(params[0]);

            return Utils.getJSONString("http://wayfarer-server.herokuapp.com/steps");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Crouton.showText(activity, "Total comppleted tasks: " + String.valueOf(stateManager.getCompletedGoals().size()), Style.INFO);
        }
    }


    class PostState extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            StateManager.postState(stateManager, userID);
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }



        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }
    }
                                          //TODO asdasdasd
//    private createNewSubject(){
//
//        Database db = Main.newUserDatabase("asd");
//        Survey s = new Survey(new ArrayList<Question>());
//        stateManager = new StateManager(db, s);
//        userID = db.getId();
//        Main.postState(stateManager, userID);
//    }
}
