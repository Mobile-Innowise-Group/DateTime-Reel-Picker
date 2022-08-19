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
import com.innowisegroup.reelpicker.extension.MAX_MONTH
import com.innowisegroup.reelpicker.extension.MIN_DAY
import com.innowisegroup.reelpicker.extension.MIN_MONTH
import com.innowisegroup.reelpicker.extension.WRAP_SELECTION_WHEEL
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_DATE_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_DATE_TAB_TITLE_REQUEST_KEY
import java.util.*

internal class DatePickerFragment : Fragment() {

    private lateinit var day: CustomNumberPicker
    private lateinit var month: CustomNumberPicker
    private lateinit var year: CustomNumberPicker

    var dateStub: TextView? = null

    private lateinit var localDate: LocalDate
    private lateinit var minLocalDate: LocalDate
    private lateinit var maxLocalDate: LocalDate
    private var wrapSelectionWheel = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_date_picker, container, false)
        applyArguments()
        applySavedStateIfNeeded(savedInstanceState)
        initializeView(view)
        refreshDateValue(localDate)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_DATE, localDate)
    }

    private fun applyArguments() =
        with(requireArguments()) {
            localDate = getSerializable(LOCAL_DATE) as LocalDate
            minLocalDate = getSerializable(MIN_LOCAL_DATE) as LocalDate
            maxLocalDate = getSerializable(MAX_LOCAL_DATE) as LocalDate
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_WHEEL)
        }

    private fun applySavedStateIfNeeded(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            localDate = savedInstanceState.getSerializable(SELECTED_DATE) as LocalDate
        }
    }

    private fun initializeView(view: View) {
        day = view.findViewById(R.id.day)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        dateStub = view.findViewById(R.id.stub)

        day.run {
            setMinMaxDay()
            value = localDate.day
            setDividerColor(
                day,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
                )
            }
            wrapSelectorWheel = wrapSelectionWheel
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshDateValue(localDate.withDayOfMonth(newVal))
            }
        }

        month.run {
            setMinMaxMonth()
            value = localDate.month
            setDividerColor(
                month,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
                )
            }
            wrapSelectorWheel = wrapSelectionWheel
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshDateValue(localDate.withMonth(newVal))
                setMinMaxDay()
                refreshDateValue(localDate.withDayOfMonth(day.value))
            }
        }

        year.run {
            minValue = minLocalDate.year
            maxValue = maxLocalDate.year
            value = localDate.year
            setDividerColor(
                year,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
            )
            setFormatter { i: Int ->
                String.format(
                    Locale.getDefault(),
                    "%04d",
                    i
                )
            }
            wrapSelectorWheel = wrapSelectionWheel
            setOnValueChangedListener { _, _, newVal: Int ->
                refreshDateValue(localDate.withYear(newVal))
                setMinMaxMonth()
                refreshDateValue(localDate.withMonth(month.value))
                setMinMaxDay()
                refreshDateValue(localDate.withDayOfMonth(day.value))
            }
        }
    }

    private fun setMinMaxMonth() =
        month.run {
            minValue =
                if (minLocalDate.year == localDate.year) minLocalDate.month else MIN_MONTH
            maxValue =
                if (maxLocalDate.year == localDate.year) maxLocalDate.month else MAX_MONTH
        }

    private fun setMinMaxDay() =
        day.run {
            minValue =
                if (localDate.year == minLocalDate.year && localDate.month == minLocalDate.month) minLocalDate.day else MIN_DAY
            maxValue =
                if (localDate.year == maxLocalDate.year && localDate.month == maxLocalDate.month) maxLocalDate.day else localDate.lengthOfMonth()
        }

    private fun refreshDateValue(newValue: LocalDate) {
        localDate = newValue

        val bundle = Bundle()
        bundle.putSerializable(UPDATE_DATE_TAB_TITLE_KEY, newValue)

        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            bundle
        )
    }

    companion object {
        internal const val LOCAL_DATE = "localDate"
        internal const val MIN_LOCAL_DATE = "minLocalDate"
        internal const val MAX_LOCAL_DATE = "maxLocalDate"
        private const val SELECTED_DATE = "selectedDate"
    }
}