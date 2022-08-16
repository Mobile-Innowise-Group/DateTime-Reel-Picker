package com.innowisegroup.reelpicker.picker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.reelpicker.R
import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_DATE_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_TIME_TAB_TITLE_REQUEST_KEY
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.UPDATE_DATE_REQUEST_KEY
import java.util.*

internal class TimePickerFragment : Fragment() {

    var timeStub: TextView? = null

    private var hours: CustomNumberPicker? = null
    private var minutes: CustomNumberPicker? = null

    lateinit var localDateTime: LocalDateTime
    private lateinit var minDateTime: LocalDateTime
    private lateinit var maxDateTime: LocalDateTime
    private lateinit var localTime: LocalTime
    private lateinit var minTime: LocalTime
    private lateinit var maxTime: LocalTime
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker, container, false)
        applyArguments()

        minTime = minDateTime.toLocalTime()
        maxTime = maxDateTime.toLocalTime()
        localTime = localDateTime.toLocalTime()

        applySavedStateIfNeeded(savedInstanceState)
        setFragmentResultListeners()
        initializeView(view)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_TIME, localDateTime)
    }

    private fun applyArguments() {
        with(requireArguments()) {
            localDateTime = getSerializable(LOCAL_TIME) as LocalDateTime
            minDateTime = getSerializable(MIN_TIME) as LocalDateTime
            maxDateTime = getSerializable(MAX_TIME) as LocalDateTime
        }
    }

    private fun applySavedStateIfNeeded(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            localDateTime = savedInstanceState.getSerializable(SELECTED_TIME) as LocalDateTime
            refreshTimeValue(localDateTime.toLocalTime())
        }
    }

    private fun setFragmentResultListeners() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_DATE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            selectedDate = bundle.getSerializable(UPDATE_DATE_TAB_TITLE_KEY) as? LocalDate
        }
    }

    private fun initializeView(view: View) {
        timeStub = view.findViewById(R.id.time_stub)
        hours = view.findViewById(R.id.hours)
        minutes = view.findViewById(R.id.minutes)

        hours?.run {
            if (selectedDate != null) {
                selectedDate?.let {
                    minValue = if (it == minDateTime.toLocalDate()) minTime.hour else MIN_HOUR
                    maxValue = if (it == maxDateTime.toLocalDate()) maxTime.hour else MAX_HOUR
                }
            } else {
                minValue = minTime.hour
                maxValue = maxTime.hour
            }
            value = localTime.hour
            wrapSelectorWheel = false
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
            if (selectedDate != null) {
                selectedDate?.let {
                    minValue =
                        if (it == minDateTime.toLocalDate() && localTime.hour == minTime.hour) minTime.minute else MIN_MINUTE
                    maxValue =
                        if (it == maxDateTime.toLocalDate() && localTime.hour == maxTime.hour) maxTime.minute else MAX_MINUTE
                }
            } else {
                minValue = if (localTime.hour == minTime.hour) minTime.minute else MIN_MINUTE
                maxValue = if (localTime.hour == maxTime.hour) maxTime.minute else MAX_MINUTE
            }

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

        if (selectedDate != null) {
            selectedDate?.let {
                hours?.minValue = if ((it == minDateTime.toLocalDate())) minTime.hour else MIN_HOUR
                hours?.maxValue = if ((it == maxDateTime.toLocalDate())) maxTime.hour else MAX_HOUR
            }
        } else {
            hours?.minValue = minTime.hour
            hours?.maxValue = maxTime.hour
        }

        if (selectedDate != null) {
            selectedDate?.let {
                minutes?.minValue =
                    if (it == minDateTime.toLocalDate() && localTime.hour == minTime.hour) minTime.minute else MIN_MINUTE
                minutes?.maxValue =
                    if (it == maxDateTime.toLocalDate() && localTime.hour == maxTime.hour) maxTime.minute else MAX_MINUTE
            }
        } else {
            minutes?.minValue = if (localTime.hour == minTime.hour) minTime.minute else MIN_MINUTE
            minutes?.maxValue = if (localTime.hour == maxTime.hour) maxTime.minute else MAX_MINUTE
        }

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
        internal const val MIN_TIME = "minTime"
        internal const val MAX_TIME = "maxTime"
        private const val SELECTED_TIME = "selectedTime"
    }
}