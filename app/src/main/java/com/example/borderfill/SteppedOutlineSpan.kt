package com.example.borderfill

import android.graphics.*
import android.text.Layout
import android.text.style.LineBackgroundSpan
import kotlin.math.abs

class SteppedOutlineSpan(
    backgroundColor: Int,
    private val padding: Float,
    private val radius: Float,
    private val alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
) : LineBackgroundSpan {

    companion object {
        private const val NO_INIT = -1f
    }

    private val rect = RectF()
    private val outerPaint = Paint().apply {
        color = backgroundColor
        isAntiAlias = true
    }
    private val innerPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }
    private val path = Path()

    private val doublePadding = padding * 2f

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
        val doubleRadius = 3f * radius
        val diffIsShort = widthDiff <= doubleRadius

        val width = if (lineNumber == 0) {
            actualWidth
        } else if ((actualWidth < prevWidth) && diffIsShort) {
            prevWidth
        } else if ((actualWidth > prevWidth) && diffIsShort) {
            actualWidth + (3f * radius)
        } else {
            actualWidth
        }

        drawOuterShape(c, right, top, bottom, lineNumber, width)
        drawCutOutShape(c, right, top, bottom, lineNumber, width)
    }

    private fun drawOuterShape(
        c: Canvas,
        right: Int,
        top: Int,
        bottom: Int,
        lineNumber: Int,
        width: Float) {

        val shiftLeft: Float
        val shiftRight: Float

        when (alignment) {
            Layout.Alignment.ALIGN_NORMAL -> {
                shiftLeft = 0f - doublePadding
                shiftRight = width + shiftLeft
            }

            Layout.Alignment.ALIGN_OPPOSITE -> {
                shiftLeft = right - width + doublePadding
                shiftRight = (right + doublePadding).toFloat()
            }
            else -> {
                shiftLeft = (right - width) / 2
                shiftRight = right - shiftLeft
            }
        }

        rect.set(shiftLeft, top.toFloat() - doublePadding, shiftRight, bottom.toFloat() - doublePadding)

        c.drawRoundRect(rect, radius, radius, outerPaint)

        if (lineNumber > 0) {
            when (alignment) {
                Layout.Alignment.ALIGN_NORMAL -> {
                    drawLeftStraightLineShape(c, rect, radius, outerPaint)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated270Shape(c, rect, radius, outerPaint)
                        prevWidth > width -> drawLowerCaseRShape(c, rect, radius, outerPaint)
                        else -> drawRightStraightLineShape(c, rect, radius, outerPaint)
                    }
                }
                Layout.Alignment.ALIGN_CENTER -> {
                    when {
                        prevWidth < width -> {
                            drawLowerCaseRRotated180Shape(c, rect, radius, outerPaint)
                            drawLowerCaseRRotated270Shape(c, rect, radius, outerPaint)

                        }
                        prevWidth > width -> {
                            drawLowerCaseRRotated90Shape(c, rect, radius, outerPaint)
                            drawLowerCaseRShape(c, rect, radius, outerPaint)
                        }
                        else -> {
                            drawLeftStraightLineShape(c, rect, radius, outerPaint)
                            drawRightStraightLineShape(c, rect, radius, outerPaint)
                        }
                    }
                }
                Layout.Alignment.ALIGN_OPPOSITE -> {
                    drawRightStraightLineShape(c, rect, radius, outerPaint)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated180Shape(c, rect, radius, outerPaint)
                        prevWidth > width -> drawLowerCaseRRotated90Shape(c, rect, radius, outerPaint)
                        else -> drawLeftStraightLineShape(c, rect, radius, outerPaint)
                    }
                }
            }
        }

        prevWidth = width
        prevLeft = rect.left
        prevRight = rect.right
    }

    private fun drawCutOutShape(
        c: Canvas,
        right: Int,
        top: Int,
        bottom: Int,
        lineNumber: Int,
        width: Float) {

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

        c.drawRoundRect(rect, radius, radius, innerPaint)

        if (lineNumber > 0) {
            when (alignment) {
                Layout.Alignment.ALIGN_NORMAL -> {
                    drawLeftStraightLineShape(c, rect, radius, innerPaint)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated270Shape(c, rect, radius, innerPaint)
                        prevWidth > width -> drawLowerCaseRShape(c, rect, radius, innerPaint)
                        else -> drawRightStraightLineShape(c, rect, radius, innerPaint)
                    }
                }
                Layout.Alignment.ALIGN_CENTER -> {
                    when {
                        prevWidth < width -> {
                            drawLowerCaseRRotated180Shape(c, rect, radius, innerPaint)
                            drawLowerCaseRRotated270Shape(c, rect, radius, innerPaint)

                        }
                        prevWidth > width -> {
                            drawLowerCaseRRotated90Shape(c, rect, radius, innerPaint)
                            drawLowerCaseRShape(c, rect, radius, innerPaint)
                        }
                        else -> {
                            drawLeftStraightLineShape(c, rect, radius, innerPaint)
                            drawRightStraightLineShape(c, rect, radius, innerPaint)
                        }
                    }
                }
                Layout.Alignment.ALIGN_OPPOSITE -> {
                    drawRightStraightLineShape(c, rect, radius, innerPaint)
                    when {
                        prevWidth < width -> drawLowerCaseRRotated180Shape(c, rect, radius, innerPaint)
                        prevWidth > width -> drawLowerCaseRRotated90Shape(c, rect, radius, innerPaint)
                        else -> drawLeftStraightLineShape(c, rect, radius, innerPaint)
                    }
                }
            }
        }

        prevWidth = width
        prevLeft = rect.left
        prevRight = rect.right
    }


    private fun drawLeftStraightLineShape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
        path.reset()
        path.moveTo(rect.left, rect.top + radius)
        path.lineTo(rect.left, rect.top - radius)
        path.lineTo(rect.left + radius, rect.top)
        path.lineTo(rect.left, rect.top + radius)

        c.drawPath(path, paint)
    }

    private fun drawRightStraightLineShape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
        path.reset()
        path.moveTo(rect.right, rect.top - radius)
        path.lineTo(rect.right, rect.top + radius)
        path.lineTo(rect.right - radius, rect.top)
        path.lineTo(rect.right, rect.top - radius)

        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRShape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
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

    private fun drawLowerCaseRRotated180Shape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
        path.reset()
        path.lineTo(prevLeft - radius, rect.top)
        path.moveTo(prevLeft + radius, rect.top)
        path.lineTo(prevLeft, rect.top - radius)
        path.cubicTo(
            prevLeft, rect.top - radius,
            prevLeft, rect.top,
            prevLeft - radius, rect.top
        )

        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated90Shape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
        path.reset()
        path.lineTo(rect.left - radius, rect.top)
        path.moveTo(rect.left + radius, rect.top)
        path.lineTo(rect.left, rect.top + radius)
        path.cubicTo(
            rect.left, rect.top + radius,
            rect.left, rect.top,
            rect.left - radius, rect.top
        )

        c.drawPath(path, paint)
    }

    private fun drawLowerCaseRRotated270Shape(c: Canvas, rect: RectF, radius: Float, paint: Paint) {
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