package com.ourcompany.widget.calender;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ourcompany.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by winson on 2015/2/22.
 */
public class CalendarDialog {
	private final int SHOW_DATE =0;
    public static final String SHARED_PREFER = "winson_calendar_view";

	private ViewPager calendarViewPager;
	private GridView yearGridView;
	private LinearLayout weekTitleLayout;

	private ImageView closeBtn;
	private TextView titleTv;
	private TextView todayButton;
	private View parentView;
	private AlertDialog alertDialog;
	private YearAndMonth ym = null;
	private int movePos = 0;
	List<YearAndMonth> timeData = new ArrayList<YearAndMonth>();
	private int currentPagerPos =0;
	private Handler mHandler = new Handler(Looper.myLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
         if (msg.what==SHOW_DATE){
			CalenderBean bean = (CalenderBean) msg.obj;
			 if (bean!=null) {
				 timeData = bean.getDates();
				 currentPagerPos = bean.getCurrentDate();

				 showData();

				 mHandler.postDelayed(new Runnable() {
					 @Override
					 public void run() {
						 initYearData();
					 }
				 },500);

			 }
		 }
		}
	};



	//显示数据
	private void showData() {




		// 点击今日按钮
		todayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					boolean isLunar = context.getSharedPreferences(
							SHARED_PREFER, Context.MODE_PRIVATE)
							.getBoolean("isSelectLunar", false);
					Calendar calendar = Calendar.getInstance();
					long time = calendar.getTimeInMillis();
					calendar.setTimeInMillis(time);
					if (isLunar && isShowLunar) {
						int[] date = LunarCalendarUtil.solarToLunar(
								calendar.get(Calendar.YEAR),
								calendar.get(Calendar.MONTH) + 1,
								calendar.get(Calendar.DAY_OF_MONTH));
						listener.onSelectDate(time, date[0], date[1] - 1, date[2], true);
					} else {

						listener.onSelectDate(time,
								calendar.get(Calendar.YEAR),
								calendar.get(Calendar.MONTH),
								calendar.get(Calendar.DAY_OF_MONTH), false);
					}
					alertDialog.dismiss();
				}
			}
		});

		final CalendarPagerAdapter calendarPagerAdapter = new CalendarPagerAdapter(
				context, isShowLunar, isSelectLunar, timeData, selectY,
				selectM, selectD, alertDialog, listener);
		calendarViewPager.setAdapter(calendarPagerAdapter);
		// 切换到所指定的日期所在位置
		calendarViewPager.setCurrentItem(currentPagerPos, false);
		// 显示标题

		YearAndMonth yearAndMonth = timeData.get(calendarViewPager
				.getCurrentItem());
		titleTv.setText(yearAndMonth.getYear() + "年"
				+ (yearAndMonth.getMonth() + 1) + "月");

		if (isShowLunar) {
			calendarViewPager
					.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							(int) TypedValue
									.applyDimension(
											TypedValue.COMPLEX_UNIT_DIP, 250,
											context.getResources()
													.getDisplayMetrics())));
		}

		calendarViewPager
				.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
											   float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageSelected(int position) {
						YearAndMonth yearAndMonth = timeData
								.get(calendarViewPager.getCurrentItem());
						titleTv.setText(yearAndMonth.getYear() + "年" + (yearAndMonth.getMonth() + 1) + "月");
					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

		// 是否为nongli
		// toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// // TODO Auto-generated method stub
		// calendarViewPager.getCal
		// }
		// });

		closeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
	});


		// 点击显示
		titleTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (yearGridView.getVisibility() == View.VISIBLE) {
					yearGridView.setVisibility(View.GONE);
					weekTitleLayout.setVisibility(View.VISIBLE);
					calendarViewPager.setVisibility(View.VISIBLE);
				} else {
					yearGridView.setVisibility(View.VISIBLE);
					weekTitleLayout.setVisibility(View.GONE);
					calendarViewPager.setVisibility(View.GONE);
				}
			}
		});



	}

	private void initYearData(){
		//设置高度一致
		yearGridView.getLayoutParams().height = calendarViewPager.getHeight()+weekTitleLayout.getHeight();
		// 年份gridView
		final List<Integer> years = new ArrayList<Integer>();
		final YearAdapter yearAdapter = new YearAdapter(context, years);
		yearGridView.setAdapter(yearAdapter);
		for (int i = maxYear; i >= minYear; i--) {
			years.add(i);
		}

		yearAdapter.notifyDataSetChanged();
		yearGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											final int position, long id) {

						EXECUTOR.execute(new Runnable() {
							@Override
							public void run() {


								for (int i = 0; i < timeData.size(); i++) {
									ym = timeData.get(i);
									if (ym.getYear() == years.get(position)
											&& ym.getMonth() == timeData.get(
											calendarViewPager.getCurrentItem())
											.getMonth()) {
										movePos = i;
										break;
									}
								}
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										yearGridView.setVisibility(View.GONE);
										calendarViewPager.setVisibility(View.VISIBLE);
										weekTitleLayout.setVisibility(View.VISIBLE);
										calendarViewPager.setCurrentItem(movePos, false);
										titleTv.setText(ym.getYear() + "年"
												+ (ym.getMonth() + 1) + "月");
									}
								});

							}
						});


					}
				});

	}


	/**
	 * 创建一个日历选择对话框
	 *
	 * @param context
	 * @param maxYear
	 *            最大的年份
	 * @param minYear
	 *            最小的年份
	 * @param selectY
	 *            初始化的年
	 * @param selectM
	 *            初始化的月
	 * @param selectD
	 *            初始化的日
	 * @param listener
	 *            回调
	 * @return
	 */
	private Context context;
	private boolean isShowLunar;
	private boolean isSelectLunar;
	private int maxYear;
	private int minYear;
	private int selectY;
	private int selectM;
	private int selectD;
	private OnSelectDateListener listener;
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();

	public AlertDialog getCalendarDialog(final Context context,
			final boolean isShowLunar, final boolean isSelectLunar,
			final int maxYear, final int minYear, final int selectY, final int selectM, int selectD,
			final OnSelectDateListener listener) {

		initData(context,isShowLunar,isSelectLunar,
		maxYear,  minYear, selectY, selectM, selectD, listener);

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		alertDialog = builder.create();
		alertDialog.setCancelable(false);
		parentView = View.inflate(context, R.layout.layout_canlendar, null);
		 titleTv = (TextView) parentView
				.findViewById(R.id.calendar_title_tv);


		 calendarViewPager = (ViewPager) parentView
				.findViewById(R.id.calendar_viewpager);
		yearGridView = (GridView) parentView
				.findViewById(R.id.year_gridview);
		closeBtn =  parentView
				.findViewById(R.id.btnClose);
		 todayButton = parentView
				.findViewById(R.id.calendar_today_btn);
		weekTitleLayout = (LinearLayout) parentView
				.findViewById(R.id.calender_week_title_tv);
		alertDialog.setView(parentView);
		titleTv.setText("正在加载");
		parentView.post(new Runnable() {
			@Override
			public void run() {

				initCalendarData();
			}
		});

		return alertDialog;
	}

	private void initCalendarData() {
		EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				List<YearAndMonth> timeDatas = new ArrayList<YearAndMonth>();
				int currentPagerPos = 0;

				// 加入所有范围内的年月
				for (int i = minYear; i <= maxYear; i++) {
					for (int j = 0; j < 12; j++) {
						YearAndMonth ym = new YearAndMonth();
						ym.setMonth(j);
						ym.setYear(i);
						timeDatas.add(ym);
						if (selectY == i && selectM == j) {
							// 算出本月所在位置
							currentPagerPos = timeDatas.size() - 1;
						}

					}
				}
				CalenderBean bean = new CalenderBean(currentPagerPos,timeDatas);
				Message message = Message.obtain();
				message.obj = bean;
				message.what = SHOW_DATE;
				mHandler.sendMessage(message);
			}
		});
	}

	private void initData(Context context, boolean isShowLunar, boolean isSelectLunar,
						  int maxYear, int minYear, int selectY, int selectM,
						  int selectD, OnSelectDateListener listener) {
		this.context = context;
		this.isShowLunar = isShowLunar;
		this.isSelectLunar = isSelectLunar;
		this.maxYear = maxYear;
		this.minYear = minYear;
		this.selectY = selectY;
		this.selectM = selectM;
		this.selectD = selectD;
		this.listener = listener;
	}

	public interface OnSelectDateListener {
		public void onSelectDate(long time, int year, int month, int day,
                                 boolean isLunar);
	}
}
