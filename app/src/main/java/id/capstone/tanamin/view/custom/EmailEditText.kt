package id.capstone.tanamin.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import id.capstone.tanamin.R
import java.util.regex.Matcher
import java.util.regex.Pattern

class EmailEditText : AppCompatEditText, OnTouchListener {

    private lateinit var clearButtonImage: Drawable
    private lateinit var emailImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.email_input)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
        emailImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    showClearButton()
                }else{
                    hideClearButton()
                }
            }
            override fun afterTextChanged(s: Editable) {
                val pattern: Pattern = Pattern.compile(REGEX_EMAIL)
                val matcher: Matcher = pattern.matcher(s)
                if(!matcher.matches()){
                    setError(context.getString(R.string.email_error), null)
                }else{
                    setError(null, null)
                }
            }
        })
    }

    private fun showClearButton() {
        setButtonDrawables(startOfTheText = emailImage, endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables(startOfTheText = emailImage)
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        setError(null, null)
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        setError(null, null)
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

    companion object {
        const val REGEX_EMAIL = "^[\\w!#\$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$"
    }
}