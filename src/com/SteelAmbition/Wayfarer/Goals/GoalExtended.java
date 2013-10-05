package com.SteelAmbition.Wayfarer.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.AsyncTasks.PostState;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.espiandev.showcaseview.ShowcaseView;

/**
 * Created with IntelliJ IDEA.
 * User: Patrick
 * Date: 9/09/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalExtended extends SherlockFragmentActivity {

    private TextView titleTextview;
    private TextView descriptionTextView;
    private String title;
    private String description;
    private String name;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.goal_view);


        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        name = intent.getStringExtra("name");

        titleTextview =(TextView) findViewById(R.id.goalTitle);
        descriptionTextView =(TextView) findViewById(R.id.goalDescription);


        if (titleTextview != null && null != title
                && title.trim().length() > 0) {
            titleTextview.setText(Html.fromHtml(title));
        }

        if (descriptionTextView != null && null != description
                 & description.trim().length() > 0) {
            descriptionTextView.setText(Html.fromHtml(description));

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.extended_goal_menu, menu);

        ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
        co.shotType = ShowcaseView.TYPE_ONE_SHOT;
        ShowcaseView.insertShowcaseView(ShowcaseView.ITEM_ACTION_ITEM, R.id.complete_task, this, "Complete goal", "When you have completed a goal, come to this screen and click this button to tell Wayfarer that you've done it.", co);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO fix up
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                break;

            case R.id.complete_task:
                MainActivity.stateManager.completeGoal(name);
                new PostState().execute();
                this.finish();
                break;
        }

        return true;
    }
}
