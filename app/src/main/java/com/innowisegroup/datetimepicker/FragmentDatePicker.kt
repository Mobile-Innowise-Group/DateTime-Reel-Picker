package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import java.util.*

class FragmentDatePicker : Fragment() {

    var day: CustomNumberPicker? = null

    var month: CustomNumberPicker? = null

    var year: CustomNumberPicker? = null

    var dateStub: TextView? = null

    var localDate: LocalDate? = null
    private var minLocalDate: LocalDate? = null
    private var maxLocalDate: LocalDate? = null

    private var refreshDateCallback: DateTimePickerDialog.RefreshCallback? = null
    private var wrapSelectionWheel = false

    fun init(
            localDate: LocalDate?,
            minLocalDate: LocalDate?,
            maxLocalDate: LocalDate?,
            callback: DateTimePickerDialog.RefreshCallback?,
            wrapSelectionWheel: Boolean
    ) {
        this.localDate = localDate
        this.minLocalDate = minLocalDate
                ?: LocalDate().of(
                        DAY_MIN_VALUE,
                        MONTH_MIN_VALUE,
                        DEFAULT_YEAR
                )
        this.maxLocalDate = maxLocalDate
                ?: LocalDate().now().plusYears(MAX_YEARS_INCREASE)
        this.refreshDateCallback = callback
        this.wrapSelectionWheel = wrapSelectionWheel
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_date_picker_spinner, container, false)
        day = view.findViewById(R.id.day)
        month = view.findViewById(R.id.month)
        year = view.findViewById(R.id.year)
        dateStub = view.findViewById(R.id.stub)

        day?.minValue = DAY_MIN_VALUE
        day?.maxValue = localDate?.lengthOfMonth()!!
        day?.value = localDate?.getDay()!!
        day?.setDividerColor(
                day,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        day?.setFormatter { i: Int ->
            String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
            )
        }
        day?.wrapSelectorWheel = wrapSelectionWheel
        day?.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            refreshDateValue(
                    localDate?.withDayOfMonth(newVal)!!
            )
        }

        month?.minValue = MONTH_MIN_VALUE
        month?.maxValue = MONTH_MAX_VALUE
        month?.value = localDate?.getMonth()!!
        month?.setDividerColor(
                month,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        month?.setFormatter { i: Int ->
            String.format(
                    Locale.getDefault(),
                    "%02d",
                    i
            )
        }
        month?.wrapSelectorWheel = wrapSelectionWheel
        month?.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            refreshDateValue(
                    localDate?.withMonth(newVal)!!
            )
        }

        year?.minValue = minLocalDate?.getMinYear()!!
        year?.maxValue = maxLocalDate?.getMaxYear()!!
        year?.value = localDate?.getYear()!!
        year?.setDividerColor(
                year,
                ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        year?.setFormatter { i: Int ->
            String.format(
                    Locale.getDefault(),
                    "%04d",
                    i
            )
        }
        year?.wrapSelectorWheel = wrapSelectionWheel
        year?.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            refreshDateValue(
                    localDate?.withYear(newVal)!!
            )
        }
        refreshDateValue(
                LocalDate().of(
                        localDate?.getDay()!!,
                        localDate?.getMonth()!!,
                        localDate?.getYear()!!
                )
        )
        return view
    }

    private fun refreshDateValue(newValue: LocalDate) {
        if (day == null) {
            return
        }
        localDate = newValue
        day?.maxValue = localDate?.lengthOfMonth()!!
        refreshDateCallback?.refresh()
    }

    private companion object {
        const val DAY_MIN_VALUE = 1
        const val MONTH_MIN_VALUE = 1
        const val MONTH_MAX_VALUE = 12
        const val DEFAULT_YEAR = 1900
        const val MAX_YEARS_INCREASE = 20
    }
}