package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.innowisegroup.datetimepicker.DateTimePickerDialog.Companion.UPDATE_DATE_TAB_TITLE_KEY
import com.innowisegroup.datetimepicker.DateTimePickerDialog.Companion.UPDATE_DATE_TAB_TITLE_REQUEST_KEY
import java.util.*

class FragmentDatePicker : Fragment() {

    var day: CustomNumberPicker? = null

    var month: CustomNumberPicker? = null

    var year: CustomNumberPicker? = null

    var dateStub: TextView? = null

    var localDate: LocalDate? = null
    private var minLocalDate: LocalDate? = null
    private var maxLocalDate: LocalDate? = null
    private var wrapSelectionWheel = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_date_picker_spinner, container, false)
        with(requireArguments()) {
            localDate = getSerializable(LOCAL_DATE) as? LocalDate
            minLocalDate = getSerializable(MIN_LOCAL_DATE) as? LocalDate
                ?: LocalDate.now()
            maxLocalDate = maxLocalDate
                ?: LocalDate.now().plusYears(MAX_YEARS_INCREASE)
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_BOOLEAN)
        }

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
        day?.setOnValueChangedListener { _, _, newVal: Int ->
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
        month?.setOnValueChangedListener { _, _, newVal: Int ->
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
        year?.setOnValueChangedListener { _, _, newVal: Int ->
            refreshDateValue(
                localDate?.withYear(newVal)!!
            )
        }
        return view
    }

    private fun refreshDateValue(newValue: LocalDate) {
        requireActivity().supportFragmentManager.setFragmentResult(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            Bundle().apply { putString(UPDATE_DATE_TAB_TITLE_KEY, newValue.formatDate()) })
    }

    companion object {
        private const val DAY_MIN_VALUE = 1
        private const val MONTH_MIN_VALUE = 1
        private const val MONTH_MAX_VALUE = 12
        private const val DEFAULT_YEAR = 1900
        private const val MAX_YEARS_INCREASE = 20

        private const val LOCAL_DATE = "localDate"
        private const val MIN_LOCAL_DATE = "minLocalDate"
        private const val MAX_LOCAL_DATE = "maxLocalDate"
        private const val WRAP_SELECTION_BOOLEAN = "wrapSelectionWheel"

        @JvmStatic
        fun newInstance(
            localDate: LocalDate?,
            minLocalDate: LocalDate?,
            maxLocalDate: LocalDate?,
            wrapSelectionWheel: Boolean
        ): FragmentDatePicker {
            val args = Bundle().apply {
                putSerializable(LOCAL_DATE, localDate)
                putSerializable(MIN_LOCAL_DATE, minLocalDate)
                putSerializable(MAX_LOCAL_DATE, maxLocalDate)
                putBoolean(WRAP_SELECTION_BOOLEAN, wrapSelectionWheel)
            }
            val fragment = FragmentDatePicker()
            fragment.arguments = args
            return fragment
        }
    }
}