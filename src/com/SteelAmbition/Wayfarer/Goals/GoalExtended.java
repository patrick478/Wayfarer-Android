package com.SteelAmbition.Wayfarer.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.MainActivity;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Main;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

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


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.goal_view);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                break;
            case android.R.id.checkbox:
                // app icon in action bar clicked; go home

                this.finish();
                break;
        }
        return true;
    }
}
