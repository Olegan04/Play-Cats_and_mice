package com.example.cat_and_mise.game_objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Mouse(
    val name: String = "",
    private val speed: Int = 0
) {
    var x: Float = 0f
    var y: Float = 0f
    val radius: Float = 30f
    private val image: Bitmap? = null
    private var currentSpeedX = 0f
    private var currentSpeedY = 0f
    private val acceleration = 0.3f
    private val deceleration = 0.2f


    fun update(targetX: Float, targetY: Float) {
        val dx = targetX - x
        val dy = targetY - y
        val distance = kotlin.math.sqrt(dx * dx + dy * dy)

        if (distance > 2) {
            val targetSpeedX = (dx / distance) * getGameSpeed().toFloat()
            val targetSpeedY = (dy / distance) * getGameSpeed().toFloat()

            currentSpeedX += (targetSpeedX - currentSpeedX) * acceleration
            currentSpeedY += (targetSpeedY - currentSpeedY) * acceleration
        } else {
            currentSpeedX *= (1 - deceleration)
            currentSpeedY *= (1 - deceleration)
            if (abs(currentSpeedX) < 0.1f) currentSpeedX = 0f
            if (abs(currentSpeedY) < 0.1f) currentSpeedY = 0f
        }

        x += currentSpeedX
        y += currentSpeedY
    }

    fun getGameSpeed(): Int = speed

    fun draw(canvas: Canvas, paint: Paint) {
        if (image != null) {
            canvas.drawBitmap(
                image,
                x - radius,
                y - radius,
                paint
            )
        } else {
            // Рисуем мышь
            paint.color = Color.argb(255, 128, 128, 128) // Серый
            canvas.drawCircle(x, y, radius, paint)

            // Уши
            paint.color = Color.argb(255, 200, 200, 200) // Светло-серый
            canvas.drawCircle(x - radius * 0.6f, y - radius * 0.6f, radius * 0.4f, paint)
            canvas.drawCircle(x + radius * 0.6f, y - radius * 0.6f, radius * 0.4f, paint)

            // Глаза
            paint.color = Color.BLACK
            canvas.drawCircle(x - radius * 0.3f, y - radius * 0.1f, radius * 0.15f, paint)
            canvas.drawCircle(x + radius * 0.3f, y - radius * 0.1f, radius * 0.15f, paint)

            // Нос
            paint.color = Color.argb(255, 255, 105, 180) // Розовый
            canvas.drawCircle(x, y + radius * 0.2f, radius * 0.1f, paint)

            // Хвост
            paint.color = Color.argb(255, 100, 100, 100) // Темно-серый
            paint.strokeWidth = radius * 0.3f
            canvas.drawLine(x, y + radius, x - radius * 1.5f, y + radius * 1.5f, paint)
        }
    }
    private fun abs(value: Float): Float = if (value < 0) -value else value
    fun getMouseInfo(): String {
        return "Мышь по имени $name бежит со скоростью $speed"
    }
}