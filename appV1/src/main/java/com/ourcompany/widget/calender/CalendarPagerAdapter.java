package com.ourcompany.widget.calender;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ourcompany.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by winson on 2015/2/22.
 */
public class CalendarPagerAdapter extends PagerAdapter {
	private Context context;
	private boolean isShowLunar;
	private boolean isSelectLunar;
	private List<YearAndMonth> data;
	private int selectY;
	private int selectM;
	private int selectD;
	private AlertDialog dialog;
	private CalendarDialog.OnSelectDateListener listener;
    private List<CalendarAdapter> calendarAdapterList = new ArrayList<CalendarAdapter>();

    public List<CalendarAdapter> getCalendarAdapterList() {
        return calendarAdapterList;
    }

    public void setCalendarAdapterList(List<CalendarAdapter> calendarAdapterList) {
        this.calendarAdapterList = calendarAdapterList;
    }

    public CalendarPagerAdapter(Context context, boolean isShowLunar,
			boolean isSelectLunar, List<YearAndMonth> data, int selectY,
			int selectM, int selectD, AlertDialog dialog,
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
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		GridView calenderView = (GridView) View.inflate(context,
				R.layout.layout_calendar_gridview, null);
		final CalendarAdapter calendarAdapter = new CalendarAdapter(context,
				isShowLunar, isSelectLunar, DateUtil.getDateList(
						data.get(position).getYear(), data.get(position)
								.getMonth()), selectY, selectM, selectD,
				dialog, listener);
		calenderView.setAdapter(calendarAdapter);
		if (!isShowLunar) {
			calenderView.setBackgroundColor(Color.WHITE);
		}
        calendarAdapterList.add(calendarAdapter);
		container.addView(calenderView);
		return calenderView;
	}

}
