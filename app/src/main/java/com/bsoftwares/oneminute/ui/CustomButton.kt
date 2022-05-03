package com.bsoftwares.oneminute.ui

import android.content.Context
import android.util.AttributeSet
import java.util.jar.Attributes

class CustomButton : androidx.appcompat.widget.AppCompatButton {

    var canAnimationRun = true
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs)

}