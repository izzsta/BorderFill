package com.example.borderfill

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val colors = listOf(
        Color.YELLOW,
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.GRAY,
        Color.CYAN
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawSpan.setOnClickListener { initSpannableText(Alignment.LEFT) }
        initSpannableEditText()

        leftAlign.setOnClickListener {
            spanText.gravity = Gravity.LEFT
            initSpannableText(Alignment.LEFT)
        }
        centerAlign.setOnClickListener {
            spanText.gravity = Gravity.CENTER
            initSpannableText(Alignment.CENTER)
        }
        rightAlign.setOnClickListener {
            spanText.gravity = Gravity.RIGHT
            initSpannableText(Alignment.RIGHT)
        }
    }

    private fun initSpannableText(alignment: Alignment) {
        val span = SteppedBorder(
            backgroundColor = colors.random(),
            padding = dp(5),
            radius = dp(5),
            alignment = alignment
        )

        with(spanText) {
            setShadowLayer(dp(10), 0f, 0f, 0) // it's important for padding working

            text = buildSpannedString { inSpans(span) { append(text.toString()) } }
        }
    }

    private fun initSpannableEditText() {
        val span = SteppedBorder(
            backgroundColor = Color.CYAN,
            padding = dp(5),
            radius = dp(10)
        )

        spanEditText.setShadowLayer(dp(10), 0f, 0f, 0) // it's important for padding working

        spanEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                text?.setSpan(span, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }
        })
    }

    private fun Context.dp(dp: Number): Float {
        return dp.toFloat() * resources.displayMetrics.density
    }
}

