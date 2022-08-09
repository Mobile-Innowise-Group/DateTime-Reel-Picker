package com.innowisegroup.reel_picker

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.core.content.res.ResourcesCompat
import java.lang.reflect.Field

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

    private fun updateView(view: View) {
        if (view is EditText) {
            view.textSize = TEXT_SIZE
            view.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        }
    }

    override fun onValueChange(numberPicker: NumberPicker, oldVal: Int, newVal: Int) {}

    fun setDividerColor(picker: NumberPicker?, customDrawable: Drawable?) {
        val pickerFields: Array<Field> = NumberPicker::class.java.declaredFields
        for (pf in pickerFields) {
            if (pf.name == "mSelectionDivider") {
                pf.isAccessible = true
                try {
                    pf[picker] = customDrawable
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
                break
            }
        }
    }

    private companion object {
        const val TEXT_SIZE = 28f
    }
}