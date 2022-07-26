package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.datetimepicker.DateTimePickerDialog.RefreshCallback
import org.threeten.bp.LocalDate
import java.lang.String.format
import java.util.*

class FragmentDatePicker : Fragment() {
    var day: CustomNumberPicker? = null

    var month: CustomNumberPicker? = null

    var year: CustomNumberPicker? = null

    var dateStub: TextView? = null

    var localDate: LocalDate? = null
    private var minLocalDate: LocalDate? = null
    private var maxLocalDate: LocalDate? = null
    private var refreshDateCallback: RefreshCallback? = null
    private var wrapSelectionWheel = false
    fun init(
        localDate: LocalDate?,
        minLocalDate: LocalDate?,
        maxLocalDate: LocalDate?,
        callback: RefreshCallback?,
        wrapSelectionWheel: Boolean
    ) {
        this.localDate = localDate
        this.minLocalDate = minLocalDate
            ?: LocalDate.of(DEFAULT_YEAR, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
        this.maxLocalDate = maxLocalDate
            ?: LocalDate.now().plusYears(MAX_YEARS_INCREASE.toLong())
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
        day?.value = localDate?.dayOfMonth!!
        day?.setDividerColor(
            day,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        day?.setFormatter { i: Int ->
            format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        day?.wrapSelectorWheel = wrapSelectionWheel
        day?.setOnValueChangedListener { picker: NumberPicker?, oldVal: Int, newVal: Int ->
            refreshDateValue(
                localDate!!.withDayOfMonth(newVal)
            )
        }
        month?.minValue = MONTH_MIN_VALUE
        month?.maxValue = MONTH_MAX_VALUE
        month?.value = localDate?.monthValue!!
        month?.setDividerColor(
            month,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        month?.setFormatter { i: Int ->
            format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        month?.wrapSelectorWheel = wrapSelectionWheel
        month?.setOnValueChangedListener { picker: NumberPicker?, oldVal: Int, newVal: Int ->
            refreshDateValue(
                localDate?.withMonth(newVal)!!
            )
        }
        year?.minValue = DEFAULT_YEAR
        year?.maxValue = maxLocalDate?.year!!
        year?.value = localDate?.year!!
        year?.setDividerColor(
            year,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        year?.setFormatter { i: Int ->
            format(
                Locale.getDefault(),
                "%04d",
                i
            )
        }
        year?.wrapSelectorWheel = wrapSelectionWheel
        year?.setOnValueChangedListener { picker: NumberPicker?, oldVal: Int, newVal: Int ->
            refreshDateValue(
                localDate!!.withYear(newVal)
            )
        }
        refreshDateValue(
            LocalDate.of(
                localDate?.year!!,
                localDate?.monthValue!!,
                localDate?.dayOfMonth!!
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

    companion object {
        private const val DAY_MIN_VALUE = 1
        private const val MONTH_MIN_VALUE = 1
        private const val MONTH_MAX_VALUE = 12
        private const val DEFAULT_YEAR = 1900
        private const val MAX_YEARS_INCREASE = 20
    }
}