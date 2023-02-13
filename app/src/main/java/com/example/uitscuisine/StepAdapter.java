package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StepAdapter extends BaseAdapter {
    public Context context;
    public int layout;
    public List<Step> list;

    public StepAdapter(Context context, int layout, List<Step> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView step, numStep;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.step = (TextView) view.findViewById(R.id.step);
            holder.numStep = (TextView) view.findViewById(R.id.numStep);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Step step = list.get(i);

        holder.step.setText(step.getStep());
        holder.numStep.setText(step.getNumStep() + ".");

        return view;
    }
}
