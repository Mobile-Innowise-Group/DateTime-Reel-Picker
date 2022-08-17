package com.innowisegroup.reelpicker.picker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.reelpicker.R
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_REQUEST_KEY
import java.util.*

internal class TimePickerFragment : Fragment() {

    var timeStub: TextView? = null

    private var hours: CustomNumberPicker? = null
    private var minutes: CustomNumberPicker? = null

    private lateinit var localTime: LocalTime
    private lateinit var minLocalTime: LocalTime
    private lateinit var maxLocalTime: LocalTime
    private var wrapSelectionWheel = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker, container, false)
        applyArguments()
        applySavedStateIfNeeded(savedInstanceState)

        refreshTimeValue(localTime)

        initializeView(view)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_TIME, localTime)
    }

    private fun applyArguments() {
        with(requireArguments()) {
            localTime = getSerializable(LOCAL_TIME) as? LocalTime ?: LocalTime.now()
            minLocalTime = getSerializable(MIN_LOCAL_TIME) as? LocalTime ?: MIN_DEFAULT_LOCAL_TIME
            maxLocalTime = getSerializable(MAX_LOCAL_TIME) as? LocalTime ?: MAX_DEFAULT_LOCAL_TIME
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_BOOLEAN)
        }
    }

    private fun applySavedStateIfNeeded(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            localTime = savedInstanceState.getSerializable(SELECTED_TIME) as LocalTime
        }
    }

    private fun initializeView(view: View) {
        timeStub = view.findViewById(R.id.time_stub)
        hours = view.findViewById(R.id.hours)
        minutes = view.findViewById(R.id.minutes)

        hours?.run {
            minValue = minLocalTime.hour
            maxValue = maxLocalTime.hour
            value = localTime.hour

            wrapSelectorWheel = wrapSelectionWheel
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
                refreshTimeValue(localTime.withHour(newVal))
            }
        }

        minutes?.run {
            minValue =
                if (localTime.hour == minLocalTime.hour) minLocalTime.minute else MIN_MINUTE
            maxValue =
                if (localTime.hour == maxLocalTime.hour) maxLocalTime.minute else MAX_MINUTE
            value = localTime.minute
            setDividerColor(
                minutes,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            wrapSelectorWheel = false
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
                )
            }
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshTimeValue(localTime.withMinute(newVal))
            }
        }
    }

    private fun refreshTimeValue(newValue: LocalTime) {
        localTime = newValue

        minutes?.minValue =
            if (localTime.hour == minLocalTime.hour) minLocalTime.minute else MIN_MINUTE
        minutes?.maxValue =
            if (localTime.hour == maxLocalTime.hour) maxLocalTime.minute else MAX_MINUTE
        hours?.minValue = minLocalTime.hour
        hours?.maxValue = maxLocalTime.hour

        val bundle = Bundle()
        bundle.putSerializable(UPDATE_TIME_TAB_TITLE_KEY, newValue)

        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            bundle
        )
    }

    companion object {
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 23
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

        internal const val LOCAL_TIME = "localTime"
        internal const val MIN_LOCAL_TIME = "minLocalTime"
        internal const val MAX_LOCAL_TIME = "maxLocalTime"
        private const val SELECTED_TIME = "selectedTime"

        private val MIN_DEFAULT_LOCAL_TIME = LocalTime.of(MIN_HOUR, MIN_MINUTE)
        private val MAX_DEFAULT_LOCAL_TIME = LocalTime.of(MAX_HOUR, MAX_MINUTE)

        internal const val WRAP_SELECTION_BOOLEAN = "wrapSelectionWheel"
    }
}