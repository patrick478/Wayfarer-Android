package com.SteelAmbition.Wayfarer.Dangers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Danger;

import java.util.List;


public class DangerRowAdapter extends ArrayAdapter<Danger>{



	private FragmentActivity activity;
    private List<Danger> items;
    private Danger item;

    private LayoutInflater inflater;

    public DangerRowAdapter(FragmentActivity act, List<Danger> arrayList) {
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

                final Danger danger = (Danger)item;
                view = inflater.inflate(R.layout.list_goalrow, null);
                final TextView title = (TextView)view.findViewById(R.id.goalTitle);





                if (title != null && null != danger.getName()
                        && danger.getName().trim().length() > 0) {
                    title.setText(Html.fromHtml(danger.getName()));

                }




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DangerExtended.class);
                    intent.putExtra("title", danger.getName());
                    intent.putExtra("description", danger.getRelevantQuestion().getQuestion());
                    intent.putExtra("name", danger.getName());
                    getContext().startActivity(intent);
                }
            });


            }


        return view;
    }

}
