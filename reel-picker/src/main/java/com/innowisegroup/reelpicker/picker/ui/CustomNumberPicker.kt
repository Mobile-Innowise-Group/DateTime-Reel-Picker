package com.innowisegroup.reelpicker.picker.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.core.content.res.ResourcesCompat
import com.innowisegroup.reelpicker.R
import java.lang.reflect.Field
import java.util.*

internal class CustomNumberPicker(
    context: Context,
    attrs: AttributeSet?
) : NumberPicker(context, attrs), OnValueChangeListener {

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    override fun onValueChange(numberPicker: NumberPicker, oldVal: Int, newVal: Int) {}

    internal fun setDividerColor(context: Context) {
        val pickerFields: Array<Field> = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    pf[this] = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.number_picker_divider_color,
                        context.theme
                    )
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
                break
            }
        }
    }

    internal fun setDefaultFormatter() {
        setFormatter { i: Int ->
            String.format(
                Locale.getDefault(),
                "%02d",
                i
            )
        }
    }

    private fun updateView(view: View) {
        if (view is EditText) {
            view.textSize = TEXT_SIZE
            view.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        }
    }

    private companion object {
        const val TEXT_SIZE = 28f
    }
}