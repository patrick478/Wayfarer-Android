package com.SteelAmbition.Wayfarer.Tasks;

import java.util.List;

import android.content.Intent;
import com.SteelAmbition.Wayfarer.R;



import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.SteelAmbition.Wayfarer.Tasks.Task;


public class TaskRowAdapter extends ArrayAdapter<Task>{



	private FragmentActivity activity;
    private List<Task> items;
    private Task item;

    private LayoutInflater inflater;

    public TaskRowAdapter(FragmentActivity act, List<Task> arrayList) {
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

                final Task taskItem = (Task)item;
                view = inflater.inflate(R.layout.list_taskrow, null);
                final TextView title = (TextView)view.findViewById(R.id.tasktitle);





                if (title != null && null != taskItem.title
                        && taskItem.title.trim().length() > 0) {
                    title.setText(Html.fromHtml(taskItem.title));
                }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TaskExtended.class);
                    intent.putExtra("title",taskItem.title);
                    intent.putExtra("description", taskItem.description);
                    getContext().startActivity(intent);
                }
            });


            }


        return view;
    }

}
