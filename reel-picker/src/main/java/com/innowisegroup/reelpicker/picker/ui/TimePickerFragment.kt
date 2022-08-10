package com.innowisegroup.reelpicker.picker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.reelpicker.R
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_REQUEST_KEY
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.extension.formatTime
import java.util.*

internal class TimePickerFragment : Fragment() {

    var timeStub: TextView? = null

    private var hours: CustomNumberPicker? = null
    private var minutes: CustomNumberPicker? = null

    var localTime: LocalTime? = null
    private var minTime: LocalTime? = null
    private var maxTime: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker, container, false)
        localTime = requireArguments().getSerializable(LOCAL_TIME) as? LocalTime
        minTime = requireArguments().getSerializable(MIN_TIME) as? LocalTime
        maxTime = requireArguments().getSerializable(MAX_TIME) as? LocalTime

        if (savedInstanceState != null) {
            localTime = savedInstanceState.getSerializable(SELECTED_TIME) as? LocalTime
            refreshTimeValue(localTime!!)
        }

        timeStub = view.findViewById(R.id.time_stub)
        hours = view.findViewById(R.id.hours)
        minutes = view.findViewById(R.id.minutes)

        hours?.run {
            minValue = minTime?.hour ?: MIN_HOUR
            maxValue = maxTime?.hour ?: MAX_HOUR
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
            minValue = minTime?.minute ?: MIN_MINUTE
            maxValue = maxTime?.minute ?: MAX_MINUTE
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
        val bundle = Bundle()
        bundle.putString(UPDATE_TIME_TAB_TITLE_KEY, newValue.formatTime())
        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            bundle
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_TIME, localTime)
    }

    companion object {
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

        internal const val LOCAL_TIME = "localTime"
        internal const val MIN_TIME = "minTime"
        internal const val MAX_TIME = "maxTime"
        private const val SELECTED_TIME = "selectedTime"
    }
}