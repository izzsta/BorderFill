package com.example.borderfill

import android.graphics.*
import android.graphics.Typeface.NORMAL
import android.text.Layout
import android.text.style.LineBackgroundSpan
import android.util.Log
import kotlin.math.abs


class SteppedFillBorderSpan(
    backgroundColor: Int,
    private val padding: Float,
    private val radius: Float,
    private val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
) : LineBackgroundSpan {

    companion object {
        private const val NO_INIT = -1f
    }

    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        alpha = 128
        style = Paint.Style.FILL_AND_STROKE
    }

    private val rect = RectF()
    private val path = Path()

    private var prevWidth = NO_INIT
    private var prevLeft = NO_INIT
    private var prevRight = NO_INIT

    override fun drawBackground(
        c: Canvas,
        p: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lineNumber: Int
    ) {
        val actualWidth = p.measureText(text, start, end) + 2f * padding
        val widthDiff = abs(prevWidth - actualWidth)
        val doubleRadius = 2f * radius
        val diffIsShort = widthDiff <= doubleRadius

        val width = if (lineNumber == 0) {
            actualWidth
        } else if ((actualWidth < prevWidth) && diffIsShort) {
            prevWidth
        } else if ((actualWidth > prevWidth) && diffIsShort) {
            actualWidth + (2f * radius)
        } else {
            actualWidth
        }

        val shiftLeft: Float
        val shiftRight: Float

        when (alignment) {
            Layout.Alignment.ALIGN_NORMAL -> {
                shiftLeft = 0f - padding
                shiftRight = width + shiftLeft
            }

            Layout.Alignment.ALIGN_OPPOSITE -> {
                shiftLeft = right - width + padding
                shiftRight = (right + padding).toFloat()
            }
            else -> {
                shiftLeft = (right - width) / 2
                shiftRight = right - shiftLeft
            }
        }

        rect.set(shiftLeft, top.toFloat(), shiftRight, bottom.toFloat())

        c.drawRoundRect(rect, radius, radius, paint)

        if (lineNumber > 0) {
            when (alignment) {
                Layout.Alignment.ALIGN_NORMAL -> {
                    drawLeftStraightLineShape(c, rect, radius)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated270Shape(c, rect, radius)
                        prevWidth > width -> drawLowerCaseRShape(c, rect, radius)
                        else -> drawRightStraightLineShape(c, rect, radius)
                    }
                }
                Layout.Alignment.ALIGN_CENTER -> {
                    when {
                        prevWidth < width -> {
                            drawLowerCaseRRotated180Shape(c, rect, radius)
                            drawLowerCaseRRotated270Shape(c, rect, radius)

                        }
                        prevWidth > width -> {
                            drawLowerCaseRRotated90Shape(c, rect, radius)
                            drawLowerCaseRShape(c, rect, radius)
                        }
                        else -> {
                            drawLeftStraightLineShape(c, rect, radius)
                            drawRightStraightLineShape(c, rect, radius)
                        }
                    }
                }
                Layout.Alignment.ALIGN_OPPOSITE -> {
                    drawRightStraightLineShape(c, rect, radius)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated180Shape(c, rect, radius)
                        prevWidth > width -> drawLowerCaseRRotated90Shape(c, rect, radius)
                        else -> drawLeftStraightLineShape(c, rect, radius)
                    }
                }
            }
        }

        prevWidth = width
        prevLeft = rect.left
        prevRight = rect.right
    }

    private fun drawLeftStraightLineShape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
                prevLeft , rect.top - radius,
                prevLeft , rect.top,
                prevLeft + radius, rect.top
        )
        c.drawPath(path, paint)

        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft , rect.top)
        path.lineTo(prevLeft, rect.top + radius)
        path.cubicTo(
                prevLeft , rect.top + radius,
                prevLeft , rect.top,
                prevLeft + radius, rect.top
        )
        c.drawPath(path, paint)

    }

    private fun drawRightStraightLineShape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.lineTo(prevRight - radius, rect.top)
        path.moveTo(prevRight, rect.top)
        path.lineTo(prevRight, rect.top + radius)
        path.cubicTo(
                prevRight, rect.top + radius,
                prevRight, rect.top,
                prevRight - radius, rect.top
        )
        c.drawPath(path, paint)

        path.reset()
        path.lineTo(prevRight - radius, rect.top)
        path.moveTo(prevRight, rect.top)
        path.lineTo(prevRight, rect.top - radius)
        path.cubicTo(
                prevRight, rect.top - radius,
                prevRight, rect.top,
                prevRight - radius, rect.top
        )
        c.drawPath(path, paint)
    }


    private fun drawLowerCaseRShape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
                prevLeft , rect.top - radius,
                prevLeft , rect.top,
                prevLeft + radius, rect.top
        )
        c.drawPath(path, paint)

        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft , rect.top)
        path.lineTo(prevLeft, rect.top + radius)
        path.cubicTo(
                prevLeft , rect.top + radius,
                prevLeft , rect.top,
                prevLeft + radius, rect.top
        )
        c.drawPath(path, paint)
    }



    private fun drawLowerCaseRRotated90Shape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.lineTo(rect.left - radius, rect.top)
        path.moveTo(rect.left, rect.top)
        path.lineTo(rect.left, rect.top + radius)
        path.cubicTo(
            rect.left, rect.top + radius,
            rect.left, rect.top,
            rect.left - radius, rect.top
        )
        c.drawPath(path, paint)

        path.reset()
        path.moveTo(rect.left + radius, rect.top)
        path.lineTo(rect.left, rect.top)
        path.lineTo(rect.left, rect.top + radius)
        path.cubicTo(
                rect.left, rect.top + radius,
                rect.left, rect.top,
                rect.left + radius, rect.top
        )
        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated270Shape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(prevRight + radius, rect.top)
        path.lineTo(prevRight, rect.top)
        path.lineTo(prevRight, rect.top - radius)
        path.cubicTo(
            prevRight, rect.top - radius,
            prevRight, rect.top,
            prevRight + radius, rect.top
        )

        c.drawPath(path, paint)

        path.reset()
        path.lineTo(prevRight - radius, rect.top)
        path.moveTo(prevRight, rect.top)
        path.lineTo(prevRight, rect.top - radius)
        path.cubicTo(
                prevRight, rect.top - radius,
                prevRight, rect.top,
                prevRight - radius, rect.top
        )
        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated180Shape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
                prevLeft, rect.top - radius,
                prevLeft, rect.top,
                prevLeft + radius, rect.top
        )
        c.drawPath(path, paint)

        path.reset()
        path.lineTo(prevLeft - radius, rect.top)
        path.moveTo(prevLeft, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
                prevLeft, rect.top - radius,
                prevLeft, rect.top,
                prevLeft - radius, rect.top
        )
        c.drawPath(path, paint)


    }
}