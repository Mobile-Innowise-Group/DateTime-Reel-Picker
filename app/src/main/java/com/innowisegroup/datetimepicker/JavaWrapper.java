package com.innowisegroup.datetimepicker;

import androidx.fragment.app.FragmentManager;

import com.innowisegroup.reelpicker.datetime.LocalDate;
import com.innowisegroup.reelpicker.picker.ReelPicker;

class JavaWrapper {

    ReelPicker reelPicker = ReelPicker.createDateDialog(
            LocalDate.of(28, 2, 1900),
            LocalDate.of(27, 2, 1900),
            LocalDate.of(28, 2, 1900));

    void showDialog(FragmentManager fragmentManager) {
        reelPicker.showDialog(fragmentManager);
    }
}