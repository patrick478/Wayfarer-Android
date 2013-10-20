package com.SteelAmbition.Wayfarer.Authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.Questions.QuestionActivity;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Database;
import com.SteelAmbition.Wayfarer.data.Question;
import com.SteelAmbition.Wayfarer.data.StateManager;
import com.SteelAmbition.Wayfarer.data.Survey;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.github.espiandev.showcaseview.ShowcaseView;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 13/07/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateSubjectActivity extends SherlockFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.createsubject);
        ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();

        ShowcaseView.insertShowcaseViewWithType(ShowcaseView.ITEM_TITLE, android.R.id.home , this, "Add the person you care about", "This is where you tell us who you are going to be helping through a tough time. Either type their name in, or if someone else has already started using Wayfarer with your friend, ask them for their 5 digit code and type it in.", co);

        Button createSubjectButton = (Button) findViewById(R.id.btnCreateSubject);
        final EditText name = (EditText) findViewById(R.id.createsubject_name);

        final Activity activity = this;

        createSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new CreateSubject(name.getText().toString(), activity).execute();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
        }
        return true;
    }


    private class CreateSubject extends AsyncTask<String, Void, Database>{

        private final String name;

        HttpResponse response;
		private Activity activity;

        public CreateSubject(String name, Activity activity){
        	this.activity = activity;
        	this.name = name;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Database doInBackground(String... params) {
            Database db = StateManager.newUserDatabase(name);

<<<<<<< HEAD
            Survey s = db.getInitialSurvey();
=======
             Survey s = db.getInitialSurvey(); //todo FIX

            //Survey
            List<Question> qlist = new ArrayList<Question>();
            String ques = "This is a question <test>";
            List<String> alist = new ArrayList<String>();
            alist.add("Answer 1");
            alist.add("Answer 2");
            alist.add("Answer 3");
            alist.add("Answer 4");
            Question question = new Question(ques, alist);

            qlist.add(question);
            s = new Survey(qlist);
>>>>>>> 1aa2e4684ac955d2e6bb7cfae3187a09589fb6bf

            for (Question q : s.getQuestions()) {
                QuestionActivity.setQuestion(q);
            }

            MainActivity.stateManager = new StateManager(db, s, MainActivity.userID);

            StateManager.postState(MainActivity.stateManager, db.getId());

            return db;


        }

        @Override
        protected void onPostExecute(Database db) {

            SharedPreferences sharedPreferences = getSharedPreferences("subject", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putString("id", db.getId());
            MainActivity.subjectID =  db.getId();

            editor.commit();

            activity.setProgressBarIndeterminateVisibility(false);

            activity.finish();

        }
    }
}