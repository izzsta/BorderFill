package com.example.borderfill

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.text.*
import android.text.style.AlignmentSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var currentAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    private var currentPadding: Int = 5
    private var currentCornerRadius: Int = 5
    private var currentFontSize : Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpannableEditText()

        screenWidthOK.setOnClickListener {
            val width = Integer.valueOf(screenWidthET.text.toString())
            currentPadding = width / 34
            currentCornerRadius = width / 54
            currentFontSize = width / 12
            initSpannableEditText()
        }

        leftAlign.setOnClickListener {
            currentAlignment = Layout.Alignment.ALIGN_NORMAL
            initSpannableEditText()
        }
        centerAlign.setOnClickListener {
            currentAlignment = Layout.Alignment.ALIGN_CENTER
            initSpannableEditText()
        }
        rightAlign.setOnClickListener {
            currentAlignment = Layout.Alignment.ALIGN_OPPOSITE
            initSpannableEditText()
        }
    }

    private fun initSpannableEditText() {
        spanEditText.textSize = currentFontSize.pxToDp
        spanEditText.setPadding(currentPadding, currentPadding, currentPadding, currentPadding)
        spanEditText.setShadowLayer(currentPadding.toFloat(), 0f, 0f, 0) // it's important for padding working

        var existingAlignmentSpans = spanEditText.text.getSpans(0, spanEditText.text.length, AlignmentSpan::class.java)
        existingAlignmentSpans.forEach { spanEditText.text.removeSpan(it) }
        var existingBackgroundSpans = spanEditText.text.getSpans(0, spanEditText.text.length, SteppedFillBorderSpan::class.java)
        existingBackgroundSpans.forEach { spanEditText.text.removeSpan(it) }


        val outerSpan = SteppedFillBorderSpan(
                backgroundColor = Color.BLUE,
                padding = currentPadding.toFloat(),
                radius = currentCornerRadius.toFloat(),
                alignment = currentAlignment
        )

        val span = BackgroundColorSpan(
                backgroundColor = Color.BLUE,
                strokeColor = Color.BLUE,
                padding = currentPadding,
                radius = currentCornerRadius
        )

        val alignmentSpan = AlignmentSpan.Standard(currentAlignment)

        spanEditText.text?.setSpan(alignmentSpan, 0, spanEditText.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanEditText.text?.setSpan(outerSpan, 0, spanEditText.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        spanEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                var textBackgroundSpans = spanEditText.text.getSpans(0, spanEditText.text.length, SteppedFillBorderSpan::class.java)
                textBackgroundSpans.forEach { spanEditText.text.removeSpan(it) }
                var alignmentSpans = spanEditText.text.getSpans(0, spanEditText.text.length, AlignmentSpan::class.java)
                alignmentSpans.forEach { spanEditText.text.removeSpan(it) }

                text?.setSpan(alignmentSpan, 0, spanEditText.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                text?.setSpan(outerSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }
        })
    }

    val Int.pxToDp: Float
        get() = this / Resources.getSystem().displayMetrics.density

    val Int.dpToPx: Float
        get() = (this * Resources.getSystem().displayMetrics.density)
}

