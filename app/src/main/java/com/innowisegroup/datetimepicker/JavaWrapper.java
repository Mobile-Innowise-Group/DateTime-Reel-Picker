package com.innowisegroup.datetimepicker;

import android.util.Log;

import com.innowisegroup.reelpicker.datetime.LocalTime;
import com.innowisegroup.reelpicker.picker.ReelPicker;

class JavaWrapper {

    ReelPicker<LocalTime> reelPicker = ReelPicker.createTimeDialog(
            LocalTime.of(1, 3).minusMinutes(62),
            LocalTime.of(0, 0),
            LocalTime.of(1, 1).plusMinutes(30)
    ).setOnClickCallback(value -> Log.e("LocalTime", value.getHour() + ":" + value.getMinute()));
}