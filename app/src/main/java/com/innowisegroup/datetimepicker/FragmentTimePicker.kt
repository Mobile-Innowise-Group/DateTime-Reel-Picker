package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.datetimepicker.DateTimePickerDialog.Companion.UPDATE_TIME_TAB_TITLE_KEY
import com.innowisegroup.datetimepicker.DateTimePickerDialog.Companion.UPDATE_TIME_TAB_TITLE_REQUEST_KEY
import java.util.*

class FragmentTimePicker : Fragment() {

    var timeStub: TextView? = null

    var mHours: CustomNumberPicker? = null

    var mMinutes: CustomNumberPicker? = null

    var localTime: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker_spinner, container, false)
        localTime = requireArguments().getSerializable(LOCAL_TIME) as? LocalTime ?: LocalTime.now()

        timeStub = view.findViewById(R.id.time_stub)
        mHours = view.findViewById(R.id.hours)
        mMinutes = view.findViewById(R.id.minutes)

        mHours?.minValue = MIN_HOUR
        mHours?.maxValue = MAX_HOUR
        mHours?.value = localTime?.getHour()!!
        mHours?.wrapSelectorWheel = true
        mHours?.setFormatter { i: Int ->
            String.format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        mHours?.setDividerColor(
            mHours,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        mHours?.setOnValueChangedListener { _, _, newVal: Int ->
            refreshTimeValue(
                localTime?.withHour(newVal)!!
            )
        }

        mMinutes?.minValue = MIN_MINUTE
        mMinutes?.maxValue = MAX_MINUTE
        mMinutes?.value = localTime?.getMinute()!!
        mMinutes?.setDividerColor(
            mMinutes,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        mMinutes?.wrapSelectorWheel = true
        mMinutes?.setFormatter { i: Int ->
            String.format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        mMinutes?.setOnValueChangedListener { _, _, newVal: Int ->
            refreshTimeValue(
                localTime?.withMinute(newVal)!!
            )
        }
        return view
    }

    private fun refreshTimeValue(newValue: LocalTime) {
        localTime = newValue
        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            Bundle().apply { putString(UPDATE_TIME_TAB_TITLE_KEY, newValue.formatTime()) })
    }

    companion object {
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

        internal const val LOCAL_TIME = "localTime"
    }
}