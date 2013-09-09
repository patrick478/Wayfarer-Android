package Tasks;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.SteelAmbition.Wayfarer.R;



import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


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
                final TextView description = (TextView)view.findViewById(R.id.taskDescription);




                if (title != null && null != taskItem.title
                        && taskItem.title.trim().length() > 0) {
                    title.setText(Html.fromHtml(taskItem.title));
                }

                if (description != null && null != taskItem.description
                        && taskItem.description.trim().length() > 0) {
                    description.setText(Html.fromHtml(taskItem.description));
                }


            }


        return view;
    }

}
