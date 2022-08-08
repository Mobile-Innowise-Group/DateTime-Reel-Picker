package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.datetimepicker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_KEY
import com.innowisegroup.datetimepicker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_REQUEST_KEY
import java.util.*

class TimePickerFragment : Fragment() {

    var timeStub: TextView? = null

    var hours: CustomNumberPicker? = null
    var minutes: CustomNumberPicker? = null

    var localTime: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker_spinner, container, false)
        localTime = requireArguments().getSerializable(LOCAL_TIME) as? LocalTime ?: LocalTime.now()

        if (savedInstanceState != null) {
            localTime = savedInstanceState.getSerializable(SELECTED_TIME) as? LocalTime
            refreshTimeValue(localTime!!)
        }

        timeStub = view.findViewById(R.id.time_stub)
        hours = view.findViewById(R.id.hours)
        minutes = view.findViewById(R.id.minutes)

        hours?.run {
            minValue = MIN_HOUR
            maxValue = MAX_HOUR
            value = localTime?.getHour()!!
            wrapSelectorWheel = true
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
                )
            }
            setDividerColor(
                hours,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshTimeValue(
                    localTime?.withHour(newVal)!!
                )
            }
        }

        minutes?.run {
            minValue = MIN_MINUTE
            maxValue = MAX_MINUTE
            value = localTime?.getMinute()!!
            setDividerColor(
                minutes,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            wrapSelectorWheel = true
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
                )
            }
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshTimeValue(
                    localTime?.withMinute(newVal)!!
                )
            }
        }
        return view
    }

    private fun refreshTimeValue(newValue: LocalTime) {
        localTime = newValue
        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            Bundle().apply { putString(UPDATE_TIME_TAB_TITLE_KEY, newValue.formatTime()) })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putSerializable(SELECTED_TIME, localTime)
        }
    }

    companion object {
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

        internal const val LOCAL_TIME = "localTime"
        private const val SELECTED_TIME = "selectedTime"
    }
}