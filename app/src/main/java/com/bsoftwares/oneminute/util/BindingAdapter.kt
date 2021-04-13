package com.bsoftwares.oneminute.util

import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.bsoftwares.oneminute.R


@BindingAdapter(value = ["notChecked", "checked","isChecked"],requireAll = true)
fun ImageButton.updateImageButton(notChecked : Drawable, checked : Drawable,isChecked: Boolean) {
    if (isChecked)
        setImageDrawable(checked)
    else
        setImageDrawable(notChecked)

}