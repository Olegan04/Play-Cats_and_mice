package com.example.cat_and_mise.game_objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.sqrt

class Cheese (
    var x: Float,
    var y: Float,
    val radius: Float,
    private val image: Bitmap? = null,
    var isVisible: Boolean = true
) {
    fun draw(canvas: Canvas, paint: Paint) {
        if (isVisible && image != null) {
            canvas.drawBitmap(
                image,
                x - radius,
                y - radius,
                paint
            )
        } else if (isVisible) {
            // Рисуем сыр - желтый круг с дырочками
            paint.color = Color.argb(255, 255, 255, 0) // Ярко-желтый
            canvas.drawCircle(x, y, radius, paint)

            // Дырочки в сыре
            paint.color = Color.argb(255, 200, 200, 0) // Темно-желтый
            canvas.drawCircle(x - radius * 0.3f, y - radius * 0.2f, radius * 0.2f, paint)
            canvas.drawCircle(x + radius * 0.4f, y + radius * 0.1f, radius * 0.15f, paint)
            canvas.drawCircle(x - radius * 0.1f, y + radius * 0.3f, radius * 0.18f, paint)
            canvas.drawCircle(x + radius * 0.2f, y - radius * 0.3f, radius * 0.12f, paint)
        }
    }

    fun checkCollision(mouseX: Float, mouseY: Float, mouseRadius: Float): Boolean {
        if (!isVisible) return false
        val dx = x - mouseX
        val dy = y - mouseY
        val distance = sqrt(dx * dx + dy * dy)
        return distance < (radius + mouseRadius)
    }
}