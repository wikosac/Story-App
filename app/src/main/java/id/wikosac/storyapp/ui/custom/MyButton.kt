package id.wikosac.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import id.wikosac.storyapp.R

class MyButton : AppCompatButton {
    private lateinit var enabledBg: Drawable
    private lateinit var disabledBg: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setTextColor(txtColor)
        background = if(isEnabled) enabledBg else disabledBg
        textSize = 12f
        gravity = Gravity.CENTER
        text = if(isEnabled) "Sign In" else "Sign In"
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBg = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBg = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}