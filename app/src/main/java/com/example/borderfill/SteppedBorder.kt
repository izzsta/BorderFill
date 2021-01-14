package com.example.borderfill

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.text.style.LineBackgroundSpan
import kotlin.math.abs


class SteppedBorder(
    backgroundColor: Int,
    private val padding: Float,
    private val radius: Float,
    private val alignment: Alignment = Alignment.LEFT
) : LineBackgroundSpan {

    companion object {
        private const val NO_INIT = -1f
    }

    private val rect = RectF()
    private val paint = Paint().apply {
        color = backgroundColor
        isAntiAlias = true
    }
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
        val diffIsShort = widthDiff < 2f * radius

        val width = if (lineNumber == 0) {
            actualWidth
        } else if ((actualWidth < prevWidth) && diffIsShort) {
            prevWidth
        } else if ((actualWidth > prevWidth) && diffIsShort) {
            actualWidth + (2f * radius - widthDiff)
        } else {
            actualWidth
        }

        val shiftLeft = 0f - padding
        val shiftRight = width + shiftLeft

        rect.set(shiftLeft, top.toFloat(), shiftRight, bottom.toFloat())

        c.drawRoundRect(rect, radius, radius, paint)

        if (lineNumber > 0) {
            when (alignment) {
                Alignment.LEFT -> {
                    drawLeftStraightLineShape(c, rect, radius)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated270Shape(c, rect, radius)
                        prevWidth > width -> drawLowerCaseRShape(c, rect, radius)
                        else -> drawRightStraightLineShape(c, rect, radius)
                    }
                }
                Alignment.CENTER -> {
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
                Alignment.RIGHT -> {
                    drawRightStraightLineShape(c, rect, radius)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated180Shape(c, rect, radius)
                        prevWidth > width -> drawLowerCaseRRotated90Shape(c, rect, radius)
                        else -> drawRightStraightLineShape(c, rect, radius)
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
        path.moveTo(rect.left, rect.top + radius)
        path.lineTo(rect.left, rect.top - radius)
        path.lineTo(rect.left + radius, rect.top)
        path.lineTo(rect.left, rect.top + radius)

        c.drawPath(path, paint)
    }

    private fun drawRightStraightLineShape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(rect.right, rect.top - radius)
        path.lineTo(rect.right, rect.top + radius)
        path.lineTo(rect.right - radius, rect.top)
        path.lineTo(rect.right, rect.top - radius)

        c.drawPath(path, paint)
    }



    private fun drawLowerCaseRShape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(rect.right + radius, rect.top)
        path.lineTo(rect.right - radius, rect.top)
        path.lineTo(rect.right, rect.top + radius)
        path.cubicTo(
            rect.right, rect.top + radius,
            rect.right, rect.top,
            rect.right + radius, rect.top
        )

        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated180Shape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft - radius, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
            prevLeft, rect.top - radius,
            prevLeft, rect.top,
            prevLeft + radius, rect.top
        )

        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated90Shape(c: Canvas, rect: RectF, radius: Float) {
        path.reset()
        path.moveTo(rect.left + radius, rect.top)
        path.lineTo(rect.left - radius, rect.top)
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
        path.lineTo(prevRight - radius, rect.top)
        path.lineTo(prevRight, rect.top - radius)
        path.cubicTo(
            prevRight, rect.top - radius,
            prevRight, rect.top,
            prevRight + radius, rect.top
        )

        c.drawPath(path, paint)
    }
}