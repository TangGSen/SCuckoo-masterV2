package com.ourcompany.widget.calender;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class CalenderBean {
    private int currentDate;

    private List<YearAndMonth> dates;
    public CalenderBean(int currentDate,List<YearAndMonth> dates){
        this.currentDate = currentDate;
        this.dates = dates;
    }
    public int getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate = currentDate;
    }

    public List<YearAndMonth> getDates() {
        return dates;
    }

    public void setDates(List<YearAndMonth> dates) {
        this.dates = dates;
    }
}
