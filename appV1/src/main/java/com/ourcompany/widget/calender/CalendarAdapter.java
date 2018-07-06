package com.ourcompany.widget.calender;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;

import java.util.Calendar;
import java.util.List;



/**
 * Created by winson on 2015/2/22.
 */
public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<Long> data;
    private int selectY;
    private int selectM;
    private int selectD;
    private AlertDialog dialog;
    private boolean isShowLunar;
    private boolean isSelectLunar;
    private CalendarDialog.OnSelectDateListener listener;

    public CalendarAdapter(Context context, boolean isShowLunar,
                           boolean isSelectLunar, List<Long> data, int selectY, int selectM,
                           int selectD, AlertDialog dialog,
                           CalendarDialog.OnSelectDateListener listener) {
        this.context = context;
        this.isShowLunar = isShowLunar;
        this.isSelectLunar = isSelectLunar;
        this.data = data;
        this.selectY = selectY;
        this.selectM = selectM;
        this.selectD = selectD;
        this.dialog = dialog;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Long getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        isSelectLunar = context.getSharedPreferences(CalendarDialog.SHARED_PREFER, Context.MODE_PRIVATE).getBoolean("isSelectLunar", false);

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_item_calendar_grid_view, null, false);
            holder.calendarTv = (TextView) convertView
                    .findViewById(R.id.item_tv);
            holder.lunarTv = (TextView) convertView.findViewById(R.id.lunar_tv);
            holder.rootItemView =  convertView.findViewById(R.id.rootItemView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) == -1) {
            convertView.setEnabled(false);
            holder.calendarTv.setEnabled(false);
            holder.lunarTv.setEnabled(false);
            holder.calendarTv.setText("");
            holder.lunarTv.setText("");
        } else {
            convertView.setEnabled(true);
            holder.calendarTv.setEnabled(true);
            holder.lunarTv.setEnabled(true);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getItem(position));
            holder.calendarTv.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");

            if (calendar.get(Calendar.YEAR) == selectY
                    && calendar.get(Calendar.MONTH) == selectM
                    && calendar.get(Calendar.DAY_OF_MONTH) == selectD) {
                //已选中的
                convertView.setBackgroundResource(R.drawable.shape_canlender_oval_bule);
                holder.lunarTv.setTextColor(Color.WHITE);
                holder.calendarTv.setTextColor(Color.WHITE);
            } else if(calendar.get(Calendar.YEAR) < selectY
                    || (calendar.get(Calendar.MONTH) < selectM && calendar.get(Calendar.YEAR)== selectY) ||
                    (calendar.get(Calendar.DAY_OF_MONTH) < selectD && calendar.get(Calendar.MONTH) == selectM)){
                //如果比当天小的话，那么久全部灰色，并且不能选择
                convertView.setEnabled(false);
                holder.lunarTv.setTextColor(ResourceUtils.getResColor(R.color.colorSecond));
                holder.calendarTv.setTextColor(ResourceUtils.getResColor(R.color.colorSecond));

            }else {
                convertView.setBackgroundResource(R.drawable.selected_btn_rect_gray);
                if (isShowLunar) {
                    //选择农历
                    if (isSelectLunar) {
                        holder.lunarTv.setTextColor(ResourceUtils.getResColor(R.color.colorPrimaryDark));
                        holder.calendarTv.setTextColor(ResourceUtils.getResColor(R.color.text_gray));
                    }
//                    else {
//                        holder.calendarTv.setTextColor(ResourceUtils.getResColor(R.color.colorPrimaryDark));
//                        holder.lunarTv.setTextColor(Color.BLACK);
//                    }
                } else {
                    holder.calendarTv.setTextColor(Color.BLACK);
                    holder.lunarTv.setTextColor(Color.BLACK);
                }
            }


            // 显示农历
            if (isShowLunar) {
                holder.lunarTv.setVisibility(View.VISIBLE);
                int[] lunarArray = LunarCalendarUtil.solarToLunar(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH));
                String monthStr = LunarCalendarUtil
                        .numToChineseMonth(lunarArray[1]);
                String dayStr = LunarCalendarUtil
                        .numToChineseDay(lunarArray[2]);
                if (!dayStr.equals("初一")) {
                    holder.lunarTv.setText(dayStr);
                } else {
                    holder.lunarTv.setText(monthStr);
                }
            } else {
                holder.lunarTv.setVisibility(View.GONE);
            }

        }
       // holder.rootItemView.setLayoutParams(new AbsListView.LayoutParams(38, 38));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && getItem(position) != -1) {

                    if (isSelectLunar && isShowLunar) {

                        long time = getItem(position);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(time);
                        int[] date = LunarCalendarUtil.solarToLunar(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH));
                        listener.onSelectDate(time, date[0], date[1] - 1,
                                date[2], true);
                    } else {
                        long time = getItem(position);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(time);
                        listener.onSelectDate(time,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH), false);
                    }

                }
                dialog.dismiss();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView calendarTv;
        TextView lunarTv;
        LinearLayout rootItemView;
    }
}
