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
import org.threeten.bp.LocalTime
import java.lang.String.format
import java.util.*

class FragmentTimePicker : Fragment() {

    var timeStub: TextView? = null

    var mHours: CustomNumberPicker? = null

    var mMinutes: CustomNumberPicker? = null

    private var refreshCallback: RefreshCallback? = null

    var localTime: LocalTime? = null

    fun init(localTime: LocalTime?, callback: RefreshCallback?) {
        this.localTime = localTime
        this.refreshCallback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_time_picker_spinner, container, false)
        timeStub = view.findViewById(R.id.time_stub)
        mHours = view.findViewById(R.id.hours)
        mMinutes = view.findViewById(R.id.minutes)

        mHours?.minValue = MIN_HOUR
        mHours?.maxValue = MAX_HOUR
        mHours?.value = localTime?.hour!!
        mHours?.wrapSelectorWheel = true
        mHours?.setFormatter { i: Int ->
            format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        mHours?.setDividerColor(
            mHours,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        mHours?.setOnValueChangedListener { picker: NumberPicker?, oldVal: Int, newVal: Int ->
            refreshTimeValue(
                localTime?.withHour(newVal)!!
            )
        }
        mMinutes?.minValue = MIN_MINUTE
        mMinutes?.maxValue = MAX_MINUTE
        mMinutes?.value = localTime?.minute!!
        mMinutes?.setDividerColor(
            mMinutes,
            ResourcesCompat.getDrawable(resources, R.drawable.number_picker_divider_color, null)
        )
        mMinutes?.wrapSelectorWheel = true
        mMinutes?.setFormatter { i: Int ->
            format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
        mMinutes?.setOnValueChangedListener { picker: NumberPicker?, oldVal: Int, newVal: Int ->
            refreshTimeValue(
                localTime?.withMinute(newVal)!!
            )
        }
        return view
    }

    private fun refreshTimeValue(newValue: LocalTime) {
        localTime = newValue
        refreshCallback?.refresh()
    }

    private companion object {
        const val MIN_HOUR = 0
        const val MAX_HOUR = 23
        const val MIN_MINUTE = 0
        const val MAX_MINUTE = 59
    }
}