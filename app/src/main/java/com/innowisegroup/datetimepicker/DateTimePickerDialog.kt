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

class DateTimePickerDialog : DialogFragment() {

    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private val okClickCallback: OkClickCallback? = null

    private val cancelClickCallback: CancelClickCallback? = null

    private var initialLocalDateTime: LocalDateTime? = null
    private var initialLocalTime: LocalTime? = null

    private var timePickerFragment: FragmentTimePicker? = null
    private var datePickerFragment: FragmentDatePicker? = null

    private var minLocalDateTime: LocalDateTime? = null
    private var maxLocalDateTime: LocalDateTime? = null
    private var wrapSelectionWheel: Boolean = false
    private var fragmentToCreate: FragmentToCreate = FragmentToCreate.BOTH

    private var currentTimeStub = "99:99"
    private var currentDateStub = "99-99-99"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_time_picker, container, false)
        with(requireArguments()) {
            initialLocalDateTime =
                getSerializable(INITIAL_LOCAL_DATE_TIME) as? LocalDateTime
                    ?: LocalDateTime.now()
            initialLocalTime = getSerializable(INITIAL_LOCAL_TIME) as? LocalTime
                ?: LocalTime.now()
            minLocalDateTime = getSerializable(MIN_LOCAL_DATE_TIME) as? LocalDateTime
            maxLocalDateTime = getSerializable(MAX_LOCAL_DATE_TIME) as? LocalDateTime
            wrapSelectionWheel = getBoolean(WRAP_SELECTION_WHEEL)
            fragmentToCreate =
                getSerializable(FRAGMENT_TO_CREATE) as? FragmentToCreate ?: FragmentToCreate.BOTH
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
            val selectedTime = bundle.getString(UPDATE_TIME_TAB_TITLE_KEY) ?: currentTimeStub
            refresh(selectedTime)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            UPDATE_DATE_TAB_TITLE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedDate = bundle.getString(UPDATE_DATE_TAB_TITLE_KEY) ?: currentDateStub
            refresh(selectedDate)
        }

        val listOfFragments: List<Fragment> = when (fragmentToCreate) {
            FragmentToCreate.ONLY_TIME -> listOf(
                FragmentTimePicker.newInstance(
                    initialLocalDateTime?.toLocalTime()
                )
            )
            FragmentToCreate.ONLY_DATE -> listOf(
                FragmentDatePicker.newInstance(
                    initialLocalDateTime?.toLocalDate(),
                    if (minLocalDateTime == null) null else minLocalDateTime?.toLocalDate(),
                    if (maxLocalDateTime == null) null else maxLocalDateTime?.toLocalDate(),
                    wrapSelectionWheel
                )
            )
            FragmentToCreate.BOTH -> {
                listOf(
                    FragmentTimePicker.newInstance(initialLocalDateTime?.toLocalTime()),
                    FragmentDatePicker.newInstance(
                        initialLocalDateTime?.toLocalDate(),
                        if (minLocalDateTime == null) null else minLocalDateTime?.toLocalDate(),
                        if (maxLocalDateTime == null) null else maxLocalDateTime?.toLocalDate(),
                        wrapSelectionWheel
                    )
                )
            }
        }

        val adapter = PagerAdapter(requireActivity(), listOfFragments)
        (viewPager as ViewPager2).adapter = adapter
        (viewPager as ViewPager2).isUserInputEnabled = false
        TabLayoutMediator(tabLayout as TabLayout, viewPager as ViewPager2) { tab, position ->
            tab.text = when (adapter.getItemViewType(position)) {
                0 -> LocalTime.now().formatTime()
                1 -> LocalDate.now().formatDate()
                else -> currentTimeStub
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
        const val UPDATE_TIME_TAB_TITLE_REQUEST_KEY = "updateTimeTabTitleKey"
        const val UPDATE_DATE_TAB_TITLE_REQUEST_KEY = "updateDateTabTitleKey"

        const val UPDATE_TIME_TAB_TITLE_KEY = "updateTimeTabTitleKey"
        const val UPDATE_DATE_TAB_TITLE_KEY = "updateDateTabTitleKey"

        private const val DIALOG_TAG = "date_time_picker_dialog"

        private const val INITIAL_LOCAL_DATE_TIME = "initialLocalDateTime"
        private const val INITIAL_LOCAL_TIME = "initialLocalTime"
        private const val MIN_LOCAL_DATE_TIME = "minLocalDateTime"
        private const val MAX_LOCAL_DATE_TIME = "maxLocalDateTime"
        private const val WRAP_SELECTION_WHEEL = "wrapSelectionWheel"
        private const val FRAGMENT_TO_CREATE = "fragmentToCreate"

        @JvmStatic
        fun newInstance(
            initialLocalDateTime: LocalDateTime?,
            initialLocalTime: LocalTime?,
            minLocalDateTime: LocalDateTime?,
            maxLocalDateTime: LocalDateTime?,
            fragmentToCreate: FragmentToCreate,
            wrapSelectionWheel: Boolean
        ): DateTimePickerDialog {
            val args = Bundle().apply {
                putSerializable(INITIAL_LOCAL_DATE_TIME, initialLocalDateTime)
                putSerializable(INITIAL_LOCAL_TIME, initialLocalTime)
                putSerializable(MIN_LOCAL_DATE_TIME, minLocalDateTime)
                putSerializable(MAX_LOCAL_DATE_TIME, maxLocalDateTime)
                putSerializable(FRAGMENT_TO_CREATE, fragmentToCreate)
                putBoolean(WRAP_SELECTION_WHEEL, wrapSelectionWheel)
            }
            val fragment = DateTimePickerDialog()
            fragment.arguments = args
            return fragment
        }
    }
}