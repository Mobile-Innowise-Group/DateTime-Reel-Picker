package com.innowisegroup.datetimepicker;

import androidx.fragment.app.FragmentManager;

import com.innowisegroup.reelpicker.picker.ReelPicker;

class JavaWrapper {

    ReelPicker reelPicker = ReelPicker.createTimeDialog();

    void showDialog(FragmentManager fragmentManager) {
        reelPicker.showDialog(fragmentManager);
    }
}