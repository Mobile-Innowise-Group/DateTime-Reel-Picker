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
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout

class DateTimePickerDialog(
        initialLocalDateTime: LocalDateTime?,
        private val minLocalDateTime: LocalDateTime?,
        private val maxLocalDateTime: LocalDateTime?,
        private val wrapSelectionWheel: Boolean,
        private val withOnlyDatePicker: Boolean,
        private val withOnlyTimePicker: Boolean
) : DialogFragment() {

    private var tabLayout: TabLayout? = null

    private var viewPager: CustomViewPager? = null

    private val okClickCallback: OkClickCallback? = null

    private val cancelClickCallback: CancelClickCallback? = null

    private var adapter: PagerAdapter? = null

    private var initialLocalDateTime: LocalDateTime? = null

    private var timePickerFragment: FragmentTimePicker? = null
    private var datePickerFragment: FragmentDatePicker? = null

    init {
        this.initialLocalDateTime = initialLocalDateTime
                ?: LocalDateTime().now()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_date_time_picker, container, false)
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(
                ResourcesCompat.getDrawable(resources,
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

        adapter = object : PagerAdapter(childFragmentManager) {
            override fun getPageTitle(position: Int): CharSequence? =
                    when (val currentFragment = adapter?.getItem(position)) {
                        is FragmentTimePicker -> currentFragment.localTime?.formatTime()
                        is FragmentDatePicker -> currentFragment.localDate?.formatDate()
                        else -> null
                    }
        }

        if (!withOnlyDatePicker) {
            timePickerFragment = FragmentTimePicker()
            timePickerFragment?.init(
                    initialLocalDateTime?.toLocalTime(),
                    object : RefreshCallback {
                        override fun refresh() = this@DateTimePickerDialog.refresh()
                    }
            )
            (adapter as PagerAdapter).addFragment(timePickerFragment!!)
        }

        if (!withOnlyTimePicker) {
            datePickerFragment = FragmentDatePicker()
            datePickerFragment?.init(
                    initialLocalDateTime?.toLocalDate(),
                    minLocalDateTime?.toLocalDate(),
                    maxLocalDateTime?.toLocalDate(),
                    object : RefreshCallback {
                        override fun refresh() = this@DateTimePickerDialog.refresh()
                    },
                    wrapSelectionWheel
            )
            (adapter as PagerAdapter).addFragment(datePickerFragment!!)
        }
        viewPager?.adapter = adapter
        viewPager?.setPagingEnabled(false)
        tabLayout?.setupWithViewPager(viewPager)
        tabLayout?.addOnTabSelectedListener(object : TabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab) {
                datePickerFragment?.dateStub?.requestFocus()
                timePickerFragment?.timeStub?.requestFocus()
            }
        })
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
                        LocalDateTime(
                                LocalDate().now(),
                                timePickerFragment?.localTime!!
                        )
                )
                return
            }
            if (timePickerFragment == null) {
                datePickerFragment?.dateStub?.requestFocus()
                okClickCallback.onOkClick(
                        LocalDateTime(
                                datePickerFragment?.localDate!!,
                                LocalTime().now()
                        )
                )
                return
            }
            datePickerFragment?.dateStub?.requestFocus()
            timePickerFragment?.timeStub?.requestFocus()
            okClickCallback.onOkClick(
                    LocalDateTime().of(
                            datePickerFragment?.localDate!!,
                            timePickerFragment?.localTime!!
                    )
            )
        }
    }

    private fun refresh() {
        val curItem = requireNonNull(viewPager?.currentItem)
        tabLayout?.getTabAt(curItem)?.text = adapter?.getPageTitle(curItem)
    }

    interface RefreshCallback {
        fun refresh()
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

    private companion object {
        const val DIALOG_TAG = "date_time_picker_dialog"
    }
}