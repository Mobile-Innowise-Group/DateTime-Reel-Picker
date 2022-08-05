package com.innowisegroup.datetimepicker

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
import com.innowisegroup.datetimepicker.FragmentDatePicker.Companion.LOCAL_DATE
import com.innowisegroup.datetimepicker.FragmentDatePicker.Companion.MAX_LOCAL_DATE
import com.innowisegroup.datetimepicker.FragmentDatePicker.Companion.MIN_LOCAL_DATE
import com.innowisegroup.datetimepicker.FragmentDatePicker.Companion.WRAP_SELECTION_BOOLEAN
import com.innowisegroup.datetimepicker.FragmentTimePicker.Companion.LOCAL_TIME

class DateTimePickerDialog : DialogFragment() {
    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private val okClickCallback: OkClickCallback? = null

    private val cancelClickCallback: CancelClickCallback? = null

    private var timePickerFragment: FragmentTimePicker? = null
    private var datePickerFragment: FragmentDatePicker? = null

    private var initialLocalDateTime: LocalDateTime? = null
    private var minLocalDateTime: LocalDateTime? = null
    private var maxLocalDateTime: LocalDateTime? = null
    private var wrapSelectionWheel: Boolean = false
    private var pickerType: PickerType = PickerType.DATE_TIME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_time_picker, container, false)
        with(requireArguments()) {
            initialLocalDateTime =
                getSerializable(INITIAL_LOCAL_DATE_TIME) as? LocalDateTime ?: LocalDateTime.now()
            minLocalDateTime =
                getSerializable(MIN_LOCAL_DATE_TIME) as? LocalDateTime ?: MIN_DEFAULT_DATE_TIME
            maxLocalDateTime =
                getSerializable(MAX_LOCAL_DATE_TIME) as? LocalDateTime ?: MAX_DEFAULT_DATE_TIME
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_WHEEL)
            pickerType = getSerializable(FRAGMENT_TO_CREATE) as PickerType
        }

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_datetimepicker_dialog,
                null
            )
        )
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        val buttonOk = view.findViewById<Button>(R.id.btn_ok)
        val buttonCancel = view.findViewById<Button>(R.id.btn_cancel)
        buttonOk.setOnClickListener {
            onOkClick()
        }
        buttonCancel.setOnClickListener {
            onCancelClick()
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_TIME_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedTime =
                bundle.getString(UPDATE_TIME_TAB_TITLE_KEY) ?: LocalTime.now().formatTime()
            refresh(selectedTime)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedDate =
                bundle.getString(UPDATE_DATE_TAB_TITLE_KEY) ?: LocalDate.now().formatDate()
            refresh(selectedDate)
        }

        val listOfFragments: List<Fragment> = when (pickerType) {
            PickerType.TIME_ONLY -> listOf(
                FragmentTimePicker().apply {
                    arguments = Bundle().apply {
                        putSerializable(LOCAL_TIME, initialLocalDateTime?.toLocalTime())
                    }
                }
            )
            PickerType.DATE_ONLY -> listOf(
                FragmentDatePicker().apply {
                    arguments = Bundle().apply {
                        putSerializable(
                            LOCAL_DATE,
                            initialLocalDateTime?.toLocalDate()
                        )
                        putSerializable(
                            MIN_LOCAL_DATE,
                            if (minLocalDateTime == null) MIN_DEFAULT_DATE_TIME.toLocalDate() else minLocalDateTime?.toLocalDate()
                        )
                        putSerializable(
                            MAX_LOCAL_DATE,
                            if (maxLocalDateTime == null) MAX_DEFAULT_DATE_TIME.toLocalDate() else maxLocalDateTime?.toLocalDate()
                        )
                        putBoolean(WRAP_SELECTION_BOOLEAN, wrapSelectionWheel)
                    }
                }
            )
            PickerType.DATE_TIME -> {
                listOf(
                    FragmentTimePicker().apply {
                        arguments = Bundle().apply {
                            putSerializable(LOCAL_TIME, initialLocalDateTime?.toLocalTime())
                        }
                    },
                    FragmentDatePicker().apply {
                        arguments = Bundle().apply {
                            putSerializable(
                                LOCAL_DATE,
                                initialLocalDateTime?.toLocalDate()
                            )
                            putSerializable(
                                MIN_LOCAL_DATE,
                                if (minLocalDateTime == null) MIN_DEFAULT_DATE_TIME.toLocalDate() else minLocalDateTime?.toLocalDate()
                            )
                            putSerializable(
                                MAX_LOCAL_DATE,
                                if (maxLocalDateTime == null) MAX_DEFAULT_DATE_TIME.toLocalDate() else maxLocalDateTime?.toLocalDate()
                            )
                            putBoolean(WRAP_SELECTION_BOOLEAN, wrapSelectionWheel)
                        }
                    }
                )
            }
        }

        val adapter = PagerAdapter(requireActivity(), listOfFragments)
        (viewPager as ViewPager2).adapter = adapter
        (viewPager as ViewPager2).isUserInputEnabled = false
        TabLayoutMediator(tabLayout as TabLayout, viewPager as ViewPager2) { tab, position ->
            tab.text = when (adapter.getItemViewType(position)) {
                0 -> LocalTime.now().formatTime()
                else -> LocalDate.now().formatDate()
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
        return view
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
                okClickCallback.onOkClick(
                    LocalDateTime.of(
                        LocalDate.now(),
                        timePickerFragment?.localTime!!
                    )
                )
                return
            }
            if (timePickerFragment == null) {
                datePickerFragment?.dateStub?.requestFocus()
                okClickCallback.onOkClick(
                    LocalDateTime.of(
                        datePickerFragment?.localDate!!,
                        LocalTime.now()
                    )
                )
                return
            }
            datePickerFragment?.dateStub?.requestFocus()
            timePickerFragment?.timeStub?.requestFocus()
            okClickCallback.onOkClick(
                LocalDateTime.of(
                    datePickerFragment?.localDate!!,
                    timePickerFragment?.localTime!!
                )
            )
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

    fun showDialog(fragmentManager: FragmentManager) {
        this.show(fragmentManager, DIALOG_TAG)
    }

    companion object {
        private const val DIALOG_TAG = "date_time_picker_dialog"

        internal const val UPDATE_TIME_TAB_TITLE_REQUEST_KEY = "updateTimeTabTitleKey"
        internal const val UPDATE_DATE_TAB_TITLE_REQUEST_KEY = "updateDateTabTitleKey"

        internal const val UPDATE_TIME_TAB_TITLE_KEY = "updateTimeTabTitleKey"
        internal const val UPDATE_DATE_TAB_TITLE_KEY = "updateDateTabTitleKey"

        private const val INITIAL_LOCAL_DATE_TIME = "initialLocalDateTime"
        private const val MIN_LOCAL_DATE_TIME = "minLocalDateTime"
        private const val MAX_LOCAL_DATE_TIME = "maxLocalDateTime"
        private const val WRAP_SELECTION_WHEEL = "wrapSelectionWheel"
        private const val FRAGMENT_TO_CREATE = "fragmentToCreate"

        private val MIN_DEFAULT_TIME = LocalTime.of(0, 0)
        private val MAX_DEFAULT_TIME = LocalTime.of(23, 59)
        private val MIN_DEFAULT_DATE = LocalDate.of(1, 1, 1900)
        private val MAX_DEFAULT_DATE = LocalDate.of(1, 1, 2100)
        private val MIN_DEFAULT_DATE_TIME = LocalDateTime(MIN_DEFAULT_DATE, MIN_DEFAULT_TIME)
        private val MAX_DEFAULT_DATE_TIME = LocalDateTime(MAX_DEFAULT_DATE, MAX_DEFAULT_TIME)

        @JvmStatic
        @JvmOverloads
        fun createTimePickerDialog(
            initialLocalTime: LocalTime = LocalTime.now(),
            minLocalTime: LocalTime = MIN_DEFAULT_TIME,
            maxLocalTime: LocalTime = MAX_DEFAULT_TIME,
            pickerType: PickerType = PickerType.TIME_ONLY,
            wrapSelectionWheel: Boolean = false
        ): DateTimePickerDialog =
            createDateTimePickerDialog(
                LocalDateTime.of(initialLocalTime),
                LocalDateTime.of(minLocalTime),
                LocalDateTime.of(maxLocalTime),
                pickerType,
                wrapSelectionWheel
            )

        @JvmStatic
        @JvmOverloads
        fun createDatePickerDialog(
            initialLocalDate: LocalDate = LocalDate.now(),
            minLocalDateTime: LocalDate = MIN_DEFAULT_DATE,
            maxLocalDateTime: LocalDate = MAX_DEFAULT_DATE,
            pickerType: PickerType = PickerType.DATE_ONLY,
            wrapSelectionWheel: Boolean = false
        ): DateTimePickerDialog =
            createDateTimePickerDialog(
                LocalDateTime.of(initialLocalDate),
                LocalDateTime.of(minLocalDateTime),
                LocalDateTime.of(maxLocalDateTime),
                pickerType,
                wrapSelectionWheel
            )

        @JvmStatic
        @JvmOverloads
        fun createDateTimePickerDialog(
            initialLocalDateTime: LocalDateTime = LocalDateTime.now(),
            minLocalDateTime: LocalDateTime = MIN_DEFAULT_DATE_TIME,
            maxLocalDateTime: LocalDateTime = MAX_DEFAULT_DATE_TIME,
            pickerType: PickerType = PickerType.DATE_TIME,
            wrapSelectionWheel: Boolean = false
        ): DateTimePickerDialog =
            DateTimePickerDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(INITIAL_LOCAL_DATE_TIME, initialLocalDateTime)
                    putSerializable(MIN_LOCAL_DATE_TIME, minLocalDateTime)
                    putSerializable(MAX_LOCAL_DATE_TIME, maxLocalDateTime)
                    putSerializable(FRAGMENT_TO_CREATE, pickerType)
                    putBoolean(WRAP_SELECTION_WHEEL, wrapSelectionWheel)
                }
            }
    }
}