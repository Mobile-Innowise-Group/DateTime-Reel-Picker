package com.innowisegroup.datetimepicker;

import androidx.fragment.app.FragmentManager;

import com.innowisegroup.reelpicker.datetime.LocalDate;
import com.innowisegroup.reelpicker.datetime.LocalTime;
import com.innowisegroup.reelpicker.picker.ReelPicker;

class JavaWrapper {

    ReelPicker reelPicker = ReelPicker.createTimeDialog(
            LocalTime.of(1, 3).minusMinutes(62),
            LocalTime.of(0, 0),
            LocalTime.of(1, 1).plusMinutes(30)
    );

    void showDialog(FragmentManager fragmentManager) {
        reelPicker.showDialog(fragmentManager);
    }
}