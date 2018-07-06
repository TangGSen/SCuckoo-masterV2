package com.ourcompany.widget.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ourcompany.R;

import java.util.List;



/**
 * Created by winson on 2015/2/22.
 */
public class YearAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> data;

    public YearAdapter(Context context, List<Integer> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Integer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_year, null, false);
            holder.tv = (TextView) convertView.findViewById(R.id.year_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(getItem(position) + "");

        return convertView;
    }

    private static class ViewHolder {
        TextView tv;
    }
}
