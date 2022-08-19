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
import com.innowisegroup.reelpicker.extension.*
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.MAX_LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.DatePickerFragment.Companion.MIN_LOCAL_DATE
import com.innowisegroup.reelpicker.picker.ui.PagerAdapter
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.LOCAL_TIME
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.MAX_LOCAL_TIME
import com.innowisegroup.reelpicker.picker.ui.TimePickerFragment.Companion.MIN_LOCAL_TIME

class ReelPicker : DialogFragment() {
    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private val okClickCallback: OkClickCallback? = null
    private val cancelClickCallback: CancelClickCallback? = null

    private var timePickerFragment: TimePickerFragment? = null
    private var datePickerFragment: DatePickerFragment? = null

    private lateinit var initialLocalDateTime: LocalDateTime
    private lateinit var minLocalDateTime: LocalDateTime
    private lateinit var maxLocalDateTime: LocalDateTime
    private var wrapSelectionWheel: Boolean = false
    private var pickerType: PickerType = PickerType.DATE_TIME

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

    private fun applyArguments() =
        with(requireArguments()) {
            initialLocalDateTime =
                getSerializable(INITIAL_LOCAL_DATE_TIME) as LocalDateTime
            minLocalDateTime =
                getSerializable(MIN_LOCAL_DATE_TIME) as LocalDateTime
            maxLocalDateTime =
                getSerializable(MAX_LOCAL_DATE_TIME) as LocalDateTime
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
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedTime =
                bundle.getSerializable(UPDATE_TIME_TAB_TITLE_KEY) as LocalTime
            refresh(selectedTime.formatTime())
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedDate =
                bundle.getSerializable(UPDATE_DATE_TAB_TITLE_KEY) as LocalDate
            refresh(selectedDate.formatDate())
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
        (viewPager as ViewPager2).adapter = adapter
        (viewPager as ViewPager2).isUserInputEnabled = false
        TabLayoutMediator(tabLayout as TabLayout, viewPager as ViewPager2) { tab, position ->
            tab.text = when (adapter.getItemViewType(position)) {
                0 -> initialLocalDateTime.toLocalTime().formatTime()
                else -> initialLocalDateTime.toLocalDate().formatDate()
            }
        }.attach()

        tabLayout?.addOnTabSelectedListener(object : TabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab) {
                datePickerFragment?.dateStub?.requestFocus()
                timePickerFragment?.timeStub?.requestFocus()
            }
        })

        //adds vertical divider between tabs
        val root = tabLayout?.getChildAt(0)
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
            PickerType.TIME_ONLY -> listOf(createTimePickerFragment())
            PickerType.DATE_ONLY -> listOf(createDatePickerFragment())
            PickerType.DATE_TIME -> {
                listOf(
                    createTimePickerFragment(),
                    createDatePickerFragment()
                )
            }
        }

    private fun createTimePickerFragment(): TimePickerFragment =
        TimePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOCAL_TIME, initialLocalDateTime.toLocalTime())
                putSerializable(MIN_LOCAL_TIME, minLocalDateTime.toLocalTime())
                putSerializable(MAX_LOCAL_TIME, maxLocalDateTime.toLocalTime())
            }
        }

    private fun createDatePickerFragment(): DatePickerFragment =
        DatePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(LOCAL_DATE, initialLocalDateTime.toLocalDate())
                putSerializable(MIN_LOCAL_DATE, minLocalDateTime.toLocalDate())
                putSerializable(MAX_LOCAL_DATE, maxLocalDateTime.toLocalDate())
            }
        }

    private fun onCancelClick() {
        dialog?.cancel()
        cancelClickCallback?.onCancelClick()
    }

    private fun onOkClick() {
        dialog?.dismiss()
        if (okClickCallback != null) {
            if (datePickerFragment == null) {
                timePickerFragment?.timeStub?.requestFocus()
                okClickCallback.onOkClick(initialLocalDateTime)
                return
            }
            if (timePickerFragment == null) {
                datePickerFragment?.dateStub?.requestFocus()
                okClickCallback.onOkClick(initialLocalDateTime)
                return
            }
            datePickerFragment?.dateStub?.requestFocus()
            timePickerFragment?.timeStub?.requestFocus()
            okClickCallback.onOkClick(initialLocalDateTime)
        }
    }

    private fun refresh(value: String) {
        val curItem = viewPager?.currentItem
        tabLayout?.getTabAt(curItem!!)?.text = value
    }

    interface OkClickCallback {
        fun onOkClick(localDateTime: LocalDateTime?)
    }

    interface CancelClickCallback {
        fun onCancelClick()
    }

    companion object {
        private const val DIALOG_TAG = "date_time_picker_dialog"

        internal const val UPDATE_TIME_TAB_TITLE_REQUEST_KEY = "updateTimeTabTitleRequestKey"
        internal const val UPDATE_DATE_TAB_TITLE_REQUEST_KEY = "updateDateTabTitleRequestKey"

        internal const val UPDATE_TIME_TAB_TITLE_KEY = "updateTimeTabTitleKey"
        internal const val UPDATE_DATE_TAB_TITLE_KEY = "updateDateTabTitleKey"

        private const val INITIAL_LOCAL_DATE_TIME = "initialLocalDateTime"
        private const val MIN_LOCAL_DATE_TIME = "minLocalDateTime"
        private const val MAX_LOCAL_DATE_TIME = "maxLocalDateTime"
        private const val PICKER_TYPE = "pickerType"

        private val MIN_DEFAULT_LOCAL_TIME = LocalTime.of(MIN_HOUR, MIN_MINUTE)
        private val MAX_DEFAULT_LOCAL_TIME = LocalTime.of(MAX_HOUR, MAX_MINUTE)
        private val MIN_DEFAULT_LOCAL_DATE = LocalDate.of(MIN_DAY, MIN_MONTH, MIN_YEAR)
        private val MAX_DEFAULT_LOCAL_DATE = LocalDate.of(MIN_DAY, MIN_MONTH, MAX_YEAR)
        private val MIN_DEFAULT_DATE_TIME =
            LocalDateTime.of(date = MIN_DEFAULT_LOCAL_DATE, time = MIN_DEFAULT_LOCAL_TIME)
        private val MAX_DEFAULT_DATE_TIME =
            LocalDateTime.of(date = MAX_DEFAULT_LOCAL_DATE, time = MAX_DEFAULT_LOCAL_TIME)

        //need to kotlin-java interop
        @JvmStatic
        @JvmOverloads
        fun createTimeDialog(
            initialLocalTime: LocalTime? = LocalTime.now(),
            minLocalTime: LocalTime? = MIN_DEFAULT_LOCAL_TIME,
            maxLocalTime: LocalTime? = MAX_DEFAULT_LOCAL_TIME,
            wrapSelectionWheel: Boolean = false
        ): ReelPicker {
            requireNotNull(initialLocalTime) { "initialLocalTime must not be null" }
            requireNotNull(minLocalTime) { "minLocalTime must not be null" }
            requireNotNull(maxLocalTime) { "maxLocalTime must not be null" }
            return (createPickerDialog(
                LocalDateTime.of(initialLocalTime),
                LocalDateTime.of(minLocalTime),
                LocalDateTime.of(maxLocalTime),
                PickerType.TIME_ONLY,
                wrapSelectionWheel
            ))
        }

        //need to kotlin-java interop
        @JvmStatic
        @JvmOverloads
        fun createDateDialog(
            initialLocalDate: LocalDate? = LocalDate.now(),
            minLocalDate: LocalDate? = MIN_DEFAULT_LOCAL_DATE,
            maxLocalDate: LocalDate? = MAX_DEFAULT_LOCAL_DATE,
            wrapSelectionWheel: Boolean = false
        ): ReelPicker {
            requireNotNull(initialLocalDate) { "initialLocalDate must not be null" }
            requireNotNull(minLocalDate) { "minLocalDate must not be null" }
            requireNotNull(maxLocalDate) { "maxLocalDate must not be null" }
            return createPickerDialog(
                LocalDateTime.of(initialLocalDate),
                LocalDateTime.of(minLocalDate),
                LocalDateTime.of(maxLocalDate),
                PickerType.DATE_ONLY,
                wrapSelectionWheel
            )
        }

        //need to kotlin-java interop
        @JvmStatic
        @JvmOverloads
        fun createDateTimeDialog(
            initialLocalDateTime: LocalDateTime? = LocalDateTime.now(),
            minLocalDateTime: LocalDateTime? = MIN_DEFAULT_DATE_TIME,
            maxLocalDateTime: LocalDateTime? = MAX_DEFAULT_DATE_TIME,
            wrapSelectionWheel: Boolean = false
        ): ReelPicker {
            requireNotNull(initialLocalDateTime) { "initialLocalDateTime must not be null" }
            requireNotNull(minLocalDateTime) { "minLocalDateTime must not be null" }
            requireNotNull(maxLocalDateTime) { "maxLocalDateTime must not be null" }
            return createPickerDialog(
                initialLocalDateTime,
                minLocalDateTime,
                maxLocalDateTime,
                PickerType.DATE_TIME,
                wrapSelectionWheel
            )
        }

        private fun createPickerDialog(
            initialLocalDateTime: LocalDateTime,
            minLocalDateTime: LocalDateTime,
            maxLocalDateTime: LocalDateTime,
            pickerType: PickerType,
            wrapSelectionWheel: Boolean
        ): ReelPicker {
            validateInputDateTime(initialLocalDateTime, minLocalDateTime, maxLocalDateTime)
            return ReelPicker().apply {
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