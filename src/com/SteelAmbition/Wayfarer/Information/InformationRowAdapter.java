package com.SteelAmbition.Wayfarer.Information;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.Dangers.DangerExtended;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Danger;
import com.SteelAmbition.Wayfarer.data.InformCard;

import java.util.List;


public class InformationRowAdapter extends ArrayAdapter<InformCard>{



	private FragmentActivity activity;
    private List<InformCard> items;
    private InformCard item;

    private LayoutInflater inflater;

    public InformationRowAdapter(FragmentActivity act, List<InformCard> arrayList) {
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

                final InformCard informCard = (InformCard)item;
                view = inflater.inflate(R.layout.list_goalrow, null);
                final TextView title = (TextView)view.findViewById(R.id.goalTitle);





                if (title != null && null != informCard.getShortDescription()
                        && informCard.getShortDescription().trim().length() > 0) {
                    title.setText(Html.fromHtml(informCard.getShortDescription()));

                }




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), InformationExtended.class);
                    intent.putExtra("title", informCard.getShortDescription());
                    intent.putExtra("description", informCard.getLongDescription());

                    getContext().startActivity(intent);
                }
            });


            }


        return view;
    }

}
