package com.SteelAmbition.Wayfarer.Goals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Goal;

import java.util.List;


public class GoalRowAdapter extends ArrayAdapter<Goal>{



	private FragmentActivity activity;
    private List<Goal> items;
    private Goal item;

    private LayoutInflater inflater;

    public GoalRowAdapter(FragmentActivity act, List<Goal> arrayList) {
        super(act, 0, arrayList);
        this.activity = act;

        this.items = arrayList;
        inflater =  (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        item = items.get(position);

        if (item != null) {

                final Goal goalItem = (Goal)item;
                view = inflater.inflate(R.layout.list_goalrow, null);
                final TextView title = (TextView)view.findViewById(R.id.goalTitle);





                if (title != null && null != goalItem.getName()
                        && goalItem.getName().trim().length() > 0) {
                    title.setText(Html.fromHtml(goalItem.getName()));
                }

                if (item.isComplete()){

                  title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), GoalExtended.class);
                    intent.putExtra("title",goalItem.getName());
                    intent.putExtra("description", goalItem.getDescription());
                    intent.putExtra("name", goalItem.getName());
                    getContext().startActivity(intent);
                }
            });


            }


        return view;
    }

}
