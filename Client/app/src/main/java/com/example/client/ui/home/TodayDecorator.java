package com.example.client.ui.home;

import android.graphics.drawable.PaintDrawable;

import com.example.client.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayDecorator implements DayViewDecorator {

    private final CalendarDay date;

    public TodayDecorator(CalendarDay dates) {
        this.date = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {//todo make better decorator
        view.setBackgroundDrawable(new PaintDrawable(R.color.purple_700));
    }
}