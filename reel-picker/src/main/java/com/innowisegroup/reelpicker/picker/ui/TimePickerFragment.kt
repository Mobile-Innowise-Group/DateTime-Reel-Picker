package com.innowisegroup.reelpicker.picker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.innowisegroup.reelpicker.R
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.extension.MAX_MINUTE
import com.innowisegroup.reelpicker.extension.MIN_MINUTE
import com.innowisegroup.reelpicker.extension.WRAP_SELECTION_WHEEL
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.TAB_TITLE_REQUEST_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.TAB_TYPE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.TAB_VALUE_KEY
import com.innowisegroup.reelpicker.picker.TabType

internal class TimePickerFragment : Fragment() {

    private lateinit var hours: CustomNumberPicker
    private lateinit var minutes: CustomNumberPicker
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
        initializeView(view)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_TIME, localTime)
    }

    private fun applyArguments() =
        with(requireArguments()) {
            localTime = getSerializable(LOCAL_TIME) as LocalTime
            minLocalTime = getSerializable(MIN_LOCAL_TIME) as LocalTime
            maxLocalTime = getSerializable(MAX_LOCAL_TIME) as LocalTime
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_WHEEL)
        }

    private fun applySavedStateIfNeeded(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            localTime = savedInstanceState.getSerializable(SELECTED_TIME) as LocalTime
        }
    }

    private fun initializeView(view: View) {
        hours = view.findViewById(R.id.hours)
        minutes = view.findViewById(R.id.minutes)

        hours.run {
            minValue = minLocalTime.hour
            maxValue = maxLocalTime.hour
            value = localTime.hour
            wrapSelectorWheel = wrapSelectionWheel
            setDefaultFormatter()
            setDividerColor(requireContext())
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshTimeValue(localTime.withHour(newVal))
                setMinMaxMinutes()
                refreshTimeValue(localTime.withMinute(minutes.value))
            }
        }

        minutes.run {
            setMinMaxMinutes()
            value = localTime.minute
            wrapSelectorWheel = wrapSelectionWheel
            setDefaultFormatter()
            setDividerColor(requireContext())
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshTimeValue(localTime.withMinute(newVal))
            }
        }
    }

    private fun setMinMaxMinutes() =
        minutes.run {
            minValue = if (localTime.hour == minLocalTime.hour) minLocalTime.minute else MIN_MINUTE
            maxValue = if (localTime.hour == maxLocalTime.hour) maxLocalTime.minute else MAX_MINUTE
        }

    private fun refreshTimeValue(newValue: LocalTime) =
        Bundle()
            .apply {
                putSerializable(TAB_VALUE_KEY, newValue)
                putSerializable(TAB_TYPE_KEY, TabType.TIME)
            }.also {
                localTime = newValue
                requireActivity().supportFragmentManager.setFragmentResult(
                    TAB_TITLE_REQUEST_KEY,
                    it
                )
            }

    companion object {
        internal const val LOCAL_TIME = "LOCAL_TIME"
        internal const val MIN_LOCAL_TIME = "MIN_LOCAL_TIME"
        internal const val MAX_LOCAL_TIME = "MAX_LOCAL_TIME"
        private const val SELECTED_TIME = "SELECTED_TIME"
    }
}