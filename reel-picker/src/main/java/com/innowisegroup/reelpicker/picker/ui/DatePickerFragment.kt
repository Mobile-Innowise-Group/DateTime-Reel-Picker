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
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_DATE_TAB_TITLE_KEY
import com.innowisegroup.reelpicker.picker.ReelPicker.Companion.UPDATE_DATE_TAB_TITLE_REQUEST_KEY
import java.util.*

internal class DatePickerFragment : Fragment() {

    private var day: CustomNumberPicker? = null
    private var month: CustomNumberPicker? = null
    private var year: CustomNumberPicker? = null

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

        refreshDateValue(localDate)

        if (savedInstanceState != null) {
            localDate = savedInstanceState.getSerializable(SELECTED_DATE) as LocalDate
            refreshDateValue(localDate)
        }

        day = view.findViewById(R.id.day)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        dateStub = view.findViewById(R.id.stub)

        day?.run {
            minValue =
                if (minLocalDate.year == localDate.year && localDate.month == minLocalDate.month) minLocalDate.day else DAY_MIN_VALUE
            maxValue =
                if (maxLocalDate.year == localDate.year && localDate.month == maxLocalDate.month) maxLocalDate.day else localDate.lengthOfMonth()
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

        month?.run {
            minValue =
                if (minLocalDate.year == localDate.year && localDate.month == minLocalDate.month) minLocalDate.month else MONTH_MIN_VALUE
            maxValue =
                if (maxLocalDate.year == localDate.year && localDate.month == maxLocalDate.month) maxLocalDate.month else MONTH_MAX_VALUE
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
            }
        }

        year?.run {
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
            }
        }
        return view
    }

    private fun applyArguments() {
        with(requireArguments()) {
            localDate = getSerializable(LOCAL_DATE) as? LocalDate ?: LocalDate.now()
            minLocalDate = getSerializable(MIN_LOCAL_DATE) as? LocalDate ?: MIN_DEFAULT_LOCAL_DATE
            maxLocalDate = getSerializable(MAX_LOCAL_DATE) as? LocalDate ?: MAX_DEFAULT_LOCAL_DATE
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_BOOLEAN)
        }
    }

    private fun refreshDateValue(newValue: LocalDate) {
        day ?: return
        localDate = newValue
        day?.minValue =
            if (localDate.year == minLocalDate.year && localDate.month == minLocalDate.month) minLocalDate.day else DAY_MIN_VALUE
        day?.maxValue =
            if (localDate.year == maxLocalDate.year && localDate.month == maxLocalDate.month) maxLocalDate.day else localDate.lengthOfMonth()

        month?.minValue =
            if (minLocalDate.year == localDate.year && localDate.month == minLocalDate.month) minLocalDate.month else MONTH_MIN_VALUE
        month?.maxValue =
            if (maxLocalDate.year == localDate.year && localDate.month == maxLocalDate.month) maxLocalDate.month else MONTH_MAX_VALUE

        val bundle = Bundle()
        bundle.putSerializable(UPDATE_DATE_TAB_TITLE_KEY, newValue)

        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            bundle
        )

        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_DATE_REQUEST_KEY,
            bundle
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_DATE, localDate)
    }

    companion object {
        private const val DAY_MIN_VALUE = 1
        private const val MONTH_MIN_VALUE = 1
        private const val MONTH_MAX_VALUE = 12
        private const val MAX_YEARS_INCREASE = 20

        private val MIN_DEFAULT_LOCAL_DATE = LocalDate.now()
        private val MAX_DEFAULT_LOCAL_DATE = LocalDate.now().plusYears(MAX_YEARS_INCREASE)

        internal const val LOCAL_DATE = "localDate"
        internal const val MIN_LOCAL_DATE = "minLocalDate"
        internal const val MAX_LOCAL_DATE = "maxLocalDate"
        internal const val WRAP_SELECTION_BOOLEAN = "wrapSelectionWheel"

        private const val SELECTED_DATE = "selectedDate"

        internal const val UPDATE_DATE_REQUEST_KEY = "updateDateRequestKey"
    }
}