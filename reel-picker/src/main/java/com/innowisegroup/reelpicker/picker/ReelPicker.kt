package com.innowisegroup.reelpicker.picker

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.innowisegroup.reelpicker.R
import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.datetime.LocalDateTime.Companion.validateInputDateTime
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.extension.MAX_HOUR
import com.innowisegroup.reelpicker.extension.MAX_MINUTE
import com.innowisegroup.reelpicker.extension.MAX_YEAR
import com.innowisegroup.reelpicker.extension.MIN_DAY
import com.innowisegroup.reelpicker.extension.MIN_HOUR
import com.innowisegroup.reelpicker.extension.MIN_MINUTE
import com.innowisegroup.reelpicker.extension.MIN_MONTH
import com.innowisegroup.reelpicker.extension.MIN_YEAR
import com.innowisegroup.reelpicker.extension.WRAP_SELECTION_WHEEL
import com.innowisegroup.reelpicker.extension.formatDate
import com.innowisegroup.reelpicker.extension.formatTime
import com.innowisegroup.reelpicker.picker.PickerType.DATE_ONLY
import com.innowisegroup.reelpicker.picker.PickerType.DATE_TIME
import com.innowisegroup.reelpicker.picker.PickerType.TIME_ONLY
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.MAX_LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.MIN_LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.PagerAdapter
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.LOCAL_TIME
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.MAX_LOCAL_TIME
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.MIN_LOCAL_TIME

class ReelPicker<T> : DialogFragment() {

    private var okClickCallback: OkClickCallback<T>? = null
    private var cancelClickCallback: CancelClickCallback? = null
    private var wrapSelectionWheel: Boolean = false
    private var pickerType: PickerType = DATE_TIME

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var initialLocalDateTime: LocalDateTime
    private lateinit var minLocalDateTime: LocalDateTime
    private lateinit var maxLocalDateTime: LocalDateTime

    private lateinit var selectedTime: LocalTime
    private lateinit var selectedDate: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reel_picker, container, false)
        applyArguments()
        applyAttributes()
        setFragmentResultListeners()
        initializeView(view)
        return view
    }

    fun showDialog(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, DIALOG_TAG)
    }

    fun setOkClickCallback(okClickCallback: OkClickCallback<T>): ReelPicker<T> {
        this.okClickCallback = okClickCallback
        return this
    }

    fun setCancelClickCallback(cancelClickCallback: CancelClickCallback): ReelPicker<T> {
        this.cancelClickCallback = cancelClickCallback
        return this
    }

    private fun applyArguments() =
        with(requireArguments()) {
            initialLocalDateTime = getSerializable(INITIAL_LOCAL_DATE_TIME) as LocalDateTime
            minLocalDateTime = getSerializable(MIN_LOCAL_DATE_TIME) as LocalDateTime
            maxLocalDateTime = getSerializable(MAX_LOCAL_DATE_TIME) as LocalDateTime
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_WHEEL)
            pickerType = getSerializable(PICKER_TYPE) as PickerType
        }

    private fun applyAttributes() {
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_datetimepicker_dialog,
                null
            )
        )
    }

    private fun setFragmentResultListeners() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            TIME_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getSerializable(TAB_VALUE_KEY)?.let { newValue ->
                (newValue as LocalTime)
                    .also { selectedTime = it }
                    .formatTime()
                    .also { tabLayout.getTabAt(0)?.text = it }
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            DATE_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getSerializable(TAB_VALUE_KEY)?.let { newValue ->
                (newValue as LocalDate)
                    .also { selectedDate = it }
                    .formatDate()
                    .also {
                        val tabPosition = when (pickerType) {
                            DATE_ONLY -> 0
                            else -> 1
                        }
                        tabLayout.getTabAt(tabPosition)?.text = it
                    }
            }
        }
    }

    private fun initializeView(view: View) {
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        val buttonOk = view.findViewById<Button>(R.id.btn_ok)
        val buttonCancel = view.findViewById<Button>(R.id.btn_cancel)

        buttonOk.setOnClickListener { onOkClick() }
        buttonCancel.setOnClickListener { onCancelClick() }

        val fragments = createFragments()
        val adapter = PagerAdapter(requireActivity(), fragments)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            selectedTime = initialLocalDateTime.toLocalTime()
            selectedDate = initialLocalDateTime.toLocalDate()
            (selectedTime.formatTime() to selectedDate.formatDate())
                .let { (formattedTime, formattedDate) ->
                    when (pickerType) {
                        TIME_ONLY -> formattedTime
                        DATE_ONLY -> formattedDate
                        DATE_TIME -> if (position == 0) formattedTime else formattedDate
                    }.also {
                        tab.text = it
                    }
                }
        }.attach()

        //adds vertical divider between tabs
        val root = tabLayout.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(ResourcesCompat.getColor(resources, android.R.color.black, null))
            drawable.setSize(1, 1)
            root.dividerPadding = 10
            root.dividerDrawable = drawable
        }
    }

    private fun createFragments(): List<Fragment> =
        when (pickerType) {
            TIME_ONLY -> listOf(createTimePickerFragment())
            DATE_ONLY -> listOf(createDatePickerFragment())
            DATE_TIME -> listOf(createTimePickerFragment(), createDatePickerFragment())
        }

    private fun createTimePickerFragment(): TimePickerFragment =
        TimePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOCAL_TIME, initialLocalDateTime.toLocalTime())
                putSerializable(MIN_LOCAL_TIME, minLocalDateTime.toLocalTime())
                putSerializable(MAX_LOCAL_TIME, maxLocalDateTime.toLocalTime())
                putBoolean(WRAP_SELECTION_WHEEL, wrapSelectionWheel)
            }
        }

    private fun createDatePickerFragment(): DatePickerFragment =
        DatePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOCAL_DATE, initialLocalDateTime.toLocalDate())
                putSerializable(MIN_LOCAL_DATE, minLocalDateTime.toLocalDate())
                putSerializable(MAX_LOCAL_DATE, maxLocalDateTime.toLocalDate())
                putBoolean(WRAP_SELECTION_WHEEL, wrapSelectionWheel)
            }
        }

    private fun onCancelClick() {
        cancelClickCallback?.onCancelClick()
        dismiss()
    }

    @Suppress("UNCHECKED_CAST")
    private fun onOkClick() {
        when (pickerType) {
            TIME_ONLY -> okClickCallback?.onOkClick(selectedTime as T)
            DATE_ONLY -> okClickCallback?.onOkClick(selectedDate as T)
            DATE_TIME -> okClickCallback?.onOkClick(
                LocalDateTime.of(selectedDate, selectedTime) as T
            )
        }
        dismiss()
    }

    interface OkClickCallback<T> {
        fun onOkClick(value: T)
    }

    interface CancelClickCallback {
        fun onCancelClick()
    }

    companion object {
        private const val DIALOG_TAG = "DIALOG_TAG"

        internal const val TIME_TAB_TITLE_REQUEST_KEY = "TIME_TAB_TITLE_REQUEST_KEY"
        internal const val DATE_TAB_TITLE_REQUEST_KEY = "DATE_TAB_TITLE_REQUEST_KEY"

        internal const val TAB_VALUE_KEY = "TAB_TITLE_KEY"

        private const val INITIAL_LOCAL_DATE_TIME = "INITIAL_LOCAL_DATE_TIME"
        private const val MIN_LOCAL_DATE_TIME = "MIN_LOCAL_DATE_TIME"
        private const val MAX_LOCAL_DATE_TIME = "MAX_LOCAL_DATE_TIME"
        private const val PICKER_TYPE = "PICKER_TYPE"

        private val MIN_DEFAULT_LOCAL_TIME = LocalTime.of(MIN_HOUR, MIN_MINUTE)
        private val MAX_DEFAULT_LOCAL_TIME = LocalTime.of(MAX_HOUR, MAX_MINUTE)
        private val MIN_DEFAULT_LOCAL_DATE = LocalDate.of(MIN_DAY, MIN_MONTH, MIN_YEAR)
        private val MAX_DEFAULT_LOCAL_DATE = LocalDate.of(MIN_DAY, MIN_MONTH, MAX_YEAR)
        private val MIN_DEFAULT_DATE_TIME =
            LocalDateTime.of(date = MIN_DEFAULT_LOCAL_DATE, time = MIN_DEFAULT_LOCAL_TIME)
        private val MAX_DEFAULT_DATE_TIME =
            LocalDateTime.of(date = MAX_DEFAULT_LOCAL_DATE, time = MAX_DEFAULT_LOCAL_TIME)

        @JvmStatic
        @JvmOverloads
        fun createTimeDialog(
            initialLocalTime: LocalTime? = LocalTime.now(),
            minLocalTime: LocalTime? = MIN_DEFAULT_LOCAL_TIME,
            maxLocalTime: LocalTime? = MAX_DEFAULT_LOCAL_TIME,
            wrapSelectionWheel: Boolean = true
        ): ReelPicker<LocalTime> {
            requireNotNull(initialLocalTime) { "initialLocalTime must not be null" }
            requireNotNull(minLocalTime) { "minLocalTime must not be null" }
            requireNotNull(maxLocalTime) { "maxLocalTime must not be null" }
            return (createPickerDialog(
                LocalDateTime.of(initialLocalTime),
                LocalDateTime.of(minLocalTime),
                LocalDateTime.of(maxLocalTime),
                TIME_ONLY,
                wrapSelectionWheel
            ))
        }

        @JvmStatic
        @JvmOverloads
        fun createDateDialog(
            initialLocalDate: LocalDate? = LocalDate.now(),
            minLocalDate: LocalDate? = MIN_DEFAULT_LOCAL_DATE,
            maxLocalDate: LocalDate? = MAX_DEFAULT_LOCAL_DATE,
            wrapSelectionWheel: Boolean = true
        ): ReelPicker<LocalDate> {
            requireNotNull(initialLocalDate) { "initialLocalDate must not be null" }
            requireNotNull(minLocalDate) { "minLocalDate must not be null" }
            requireNotNull(maxLocalDate) { "maxLocalDate must not be null" }
            return createPickerDialog(
                LocalDateTime.of(initialLocalDate),
                LocalDateTime.of(minLocalDate),
                LocalDateTime.of(maxLocalDate),
                DATE_ONLY,
                wrapSelectionWheel
            )
        }

        @JvmStatic
        @JvmOverloads
        fun createDateTimeDialog(
            initialLocalDateTime: LocalDateTime? = LocalDateTime.now(),
            minLocalDateTime: LocalDateTime? = MIN_DEFAULT_DATE_TIME,
            maxLocalDateTime: LocalDateTime? = MAX_DEFAULT_DATE_TIME,
            wrapSelectionWheel: Boolean = true
        ): ReelPicker<LocalDateTime> {
            requireNotNull(initialLocalDateTime) { "initialLocalDateTime must not be null" }
            requireNotNull(minLocalDateTime) { "minLocalDateTime must not be null" }
            requireNotNull(maxLocalDateTime) { "maxLocalDateTime must not be null" }
            return createPickerDialog(
                initialLocalDateTime,
                minLocalDateTime,
                maxLocalDateTime,
                DATE_TIME,
                wrapSelectionWheel
            )
        }

        private fun <T> createPickerDialog(
            initialLocalDateTime: LocalDateTime,
            minLocalDateTime: LocalDateTime,
            maxLocalDateTime: LocalDateTime,
            pickerType: PickerType,
            wrapSelectionWheel: Boolean
        ): ReelPicker<T> {
            validateInputDateTime(initialLocalDateTime, minLocalDateTime, maxLocalDateTime)
            return ReelPicker<T>().apply {
                arguments = Bundle().apply {
                    putSerializable(INITIAL_LOCAL_DATE_TIME, initialLocalDateTime)
                    putSerializable(MIN_LOCAL_DATE_TIME, minLocalDateTime)
                    putSerializable(MAX_LOCAL_DATE_TIME, maxLocalDateTime)
                    putSerializable(PICKER_TYPE, pickerType)
                    putBoolean(WRAP_SELECTION_WHEEL, wrapSelectionWheel)
                }
            }
        }
    }
}