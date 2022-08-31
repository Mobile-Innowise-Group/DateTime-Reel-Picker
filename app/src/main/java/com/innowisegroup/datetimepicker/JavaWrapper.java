package com.innowisegroup.datetimepicker;

import androidx.fragment.app.FragmentManager;

import com.innowisegroup.reelpicker.datetime.LocalTime;
import com.innowisegroup.reelpicker.picker.ReelPicker;

class JavaWrapper {

    ReelPicker reelPicker = ReelPicker.createTimeDialog(
            LocalTime.of(15, 20).minusMinutes(62),
            LocalTime.of(3, 18),
            LocalTime.of(22, 52).plusMinutes(30)
    );

    void showDialog(FragmentManager fragmentManager) {
        reelPicker.showDialog(fragmentManager);
    }
}