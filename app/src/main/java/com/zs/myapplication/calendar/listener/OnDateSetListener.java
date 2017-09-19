package com.zs.myapplication.calendar.listener;


import com.zs.myapplication.calendar.TimePickerDialog;

/**
 * Created by zs on 16/4/20.
 */
public interface OnDateSetListener {

    void onDateSet(TimePickerDialog timePickerView, long millseconds);
}
