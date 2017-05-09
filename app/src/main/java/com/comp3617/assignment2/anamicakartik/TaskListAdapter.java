package com.comp3617.assignment2.anamicakartik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jkart on 2016-06-22.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private Context mCtx = null;
    private List<Task> mTasks;

    public TaskListAdapter(Context ctx, List<Task> tasks){
        super(ctx,0,tasks);
        mCtx = ctx;
        mTasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;

        if(convertView == null){
            LayoutInflater vi = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.row_layout, parent, false);
        }
        else{
            rowView = convertView;
        }

        ImageView icon = (ImageView)rowView.findViewById(R.id.imgTitle);
        TextView title = (TextView)rowView.findViewById(R.id.taskTitle);
        TextView description = (TextView)rowView.findViewById(R.id.description);
        TextView address = (TextView)rowView.findViewById(R.id.address);
        TextView status = (TextView)rowView.findViewById(R.id.status);
        TextView category = (TextView)rowView.findViewById(R.id.category);
        TextView priority = (TextView)rowView.findViewById(R.id.priority);

        Task task = mTasks.get(position);

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        address.setText(task.getAddress());
        icon.setImageResource(task.getImageResource());
        category.setText(task.getCategory());
        status.setText(task.getStatus());
        priority.setText(task.getPriority());

        return rowView;
    }

}

