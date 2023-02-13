package com.example.uitscuisine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AccountSettingAdapter extends BaseAdapter {
    public Context context;
    public int layout;
    public List<AccountSetting> list;

    public AccountSettingAdapter(Context context, int layout, List<AccountSetting> list) {
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
        TextView settingName;
        ImageView settingIcon;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.settingName = (TextView) view.findViewById(R.id.settingName);
            holder.settingIcon = (ImageView) view.findViewById(R.id.settingIcon);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        AccountSetting setting = list.get(i);

        holder.settingName.setText(setting.getSettingName());
        holder.settingIcon.setImageResource(setting.getSettingIcon());

        return view;
    }
}
