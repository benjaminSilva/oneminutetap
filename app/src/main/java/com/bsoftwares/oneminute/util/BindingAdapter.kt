package com.bsoftwares.oneminute.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.Drawable
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.databinding.BindingAdapter
import com.bsoftwares.oneminute.R
import com.bsoftwares.oneminute.model.TimerSettings
import com.bsoftwares.oneminute.ui.CustomButton
import com.bsoftwares.oneminute.viewModel.TimerViewModel


@BindingAdapter(value = ["notChecked", "checked","isChecked"],requireAll = true)
fun ImageButton.updateImageButton(notChecked : Drawable, checked : Drawable,isChecked: Boolean) {
    if (isChecked)
        setImageDrawable(checked)
    else
        setImageDrawable(notChecked)

}

@BindingAdapter("pulse")
fun CustomButton.pulse(time : Any) {
    if (canAnimationRun){
        canAnimationRun = false
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", 1.1f),
            PropertyValuesHolder.ofFloat("scaleY", 1.1f)
        )
        scaleDown.duration = 150
        scaleDown.repeatCount = 1
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.start()
        scaleDown.doOnEnd {
            canAnimationRun = true
        }
    }
    if(time is Int){
        text = context.getString(time)
    }
    if (time is String){
        text = time
        when(time.toIntOrNull()){
            in 1..5 -> {
                ObjectAnimator
                    .ofFloat(this,"translationX",0F, 25F, -25F, 25F, -25F,15F, -15F, 6F, -6F, 0F)
                    .setDuration(500)
                    .start()
            }else -> Unit
        }
    }
}

@BindingAdapter("settings","viewModel","maxTime")
fun TextView.setTextSeconds(settings : TimerSettings?, viewModel : TimerViewModel,max : Int) {
    text = if (settings?.random == true){
        if(viewModel.max.value==null)
            context.getString(R.string.currentTimer,context.getString(R.string.random))
        else
            context.getString(R.string.currentTimer,max.toString())
    }
    else
        context.getString(R.string.currentTimer,settings?.timer.toString())
}