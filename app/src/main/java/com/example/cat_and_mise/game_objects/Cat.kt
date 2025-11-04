package com.example.cat_and_mise.game_objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.*

class Cat (
    var x: Float,
    var y: Float,
    val radius: Float,
    private val image: Bitmap? = null,
    var speed: Float = 5f
) {
    private var directionX = (Math.random() * 2 - 1).toFloat()
    private var directionY = (Math.random() * 2 - 1).toFloat()
    private var targetDirectionX = directionX
    private var targetDirectionY = directionY
    private var changeDirectionCounter = 0
    private val changeDirectionInterval = 180
    private var movementPattern = (0..4).random()
    private var patternCounter = 0
    private var initialX = x
    private var initialY = y
    private var smoothFactor = 0.1f

    init {
        normalizeDirection()
        targetDirectionX = directionX
        targetDirectionY = directionY
    }

    fun update(width: Int, height: Int) {
        changeDirectionCounter++
        patternCounter++

        directionX += (targetDirectionX - directionX) * smoothFactor
        directionY += (targetDirectionY - directionY) * smoothFactor
        normalizeCurrentDirection()

        when (movementPattern) {
            0 -> randomMovement()
            1 -> circularMovement()
            2 -> zigzagMovement()
            3 -> horizontalMovement()
            4 -> verticalMovement()
        }

        if (patternCounter > 240) {
            movementPattern = (0..4).random()
            patternCounter = 0
        }

        x += directionX * speed
        y += directionY * speed

        // Обрабатываем границы с отскоком
        handleBoundaries(width, height)
    }

    private fun randomMovement() {
        // Случайное изменение направления
        if (changeDirectionCounter >= changeDirectionInterval) {
            directionX = (Math.random() * 2 - 1).toFloat()
            directionY = (Math.random() * 2 - 1).toFloat()
            normalizeTargetDirection()
            changeDirectionCounter = 0
        }
    }

    private fun circularMovement() {
        // Круговое движение вокруг начальной позиции
        val angle = patternCounter * 0.02f
        val circleRadius = 120f

        val targetX = initialX + cos(angle) * circleRadius
        val targetY = initialY + sin(angle) * circleRadius

        val dx = targetX - x
        val dy = targetY - y
        val distance = sqrt(dx * dx + dy * dy)

        if (distance > 5f) {
            targetDirectionX = dx / distance
            targetDirectionY = dy / distance
        }
    }

    private fun zigzagMovement() {
        // Зигзагообразное движение
        if (changeDirectionCounter >= 90) {
            targetDirectionX = -targetDirectionX
            targetDirectionY = (Math.random() * 2 - 1).toFloat()
            normalizeTargetDirection()
            changeDirectionCounter = 0
        }
    }

    private fun horizontalMovement() {
        // Горизонтальное движение туда-обратно
        targetDirectionY = 0f
        if (changeDirectionCounter >= changeDirectionInterval) {
            targetDirectionX = -targetDirectionX
            changeDirectionCounter = 0
        }
    }

    private fun verticalMovement() {
        // Вертикальное движение туда-обратно
        targetDirectionX = 0f
        if (changeDirectionCounter >= changeDirectionInterval) {
            targetDirectionY = -targetDirectionY
            changeDirectionCounter = 0
        }
    }

    private fun normalizeCurrentDirection() {
        val length = sqrt(directionX * directionX + directionY * directionY)
        if (length > 0) {
            directionX /= length
            directionY /= length
        }
    }

    private fun normalizeTargetDirection() {
        val length = sqrt(targetDirectionX * targetDirectionX + targetDirectionY * targetDirectionY)
        if (length > 0) {
            targetDirectionX /= length
            targetDirectionY /= length
        }
    }

    private fun normalizeDirection() {
        val length = sqrt(directionX * directionX + directionY * directionY)
        if (length > 0) {
            directionX /= length
            directionY /= length
        } else {
            directionX = (Math.random() * 2 - 1).toFloat()
            directionY = (Math.random() * 2 - 1).toFloat()
            normalizeDirection()
        }
    }

    private fun handleBoundaries(width: Int, height: Int) {
        var bounced = false

        if (x < radius) {
            x = radius
            targetDirectionX = abs(targetDirectionX)
            bounced = true
        }
        if (x > width - radius) {
            x = width - radius
            targetDirectionX = -abs(targetDirectionX)
            bounced = true
        }
        if (y < radius) {
            y = radius
            targetDirectionY = abs(targetDirectionY)
            bounced = true
        }
        if (y > height - radius) {
            y = height - radius
            targetDirectionY = -abs(targetDirectionY)
            bounced = true
        }

        if (bounced) {
            targetDirectionX += (Math.random() * 0.3 - 0.15).toFloat()
            targetDirectionY += (Math.random() * 0.3 - 0.15).toFloat()
            normalizeTargetDirection()
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        if (image != null) {
            canvas.drawBitmap(
                image,
                x - radius,
                y - radius,
                paint
            )
        } else {
            // Рисуем кота
            paint.color = Color.argb(255, 255, 165, 0) // Оранжевый
            canvas.drawCircle(x, y, radius, paint)

            // Уши
            paint.color = Color.argb(255, 255, 140, 0) // Темно-оранжевый
            canvas.drawCircle(x - radius * 0.7f, y - radius * 0.7f, radius * 0.4f, paint)
            canvas.drawCircle(x + radius * 0.7f, y - radius * 0.7f, radius * 0.4f, paint)

            // Глаза
            paint.color = Color.GREEN
            canvas.drawCircle(x - radius * 0.4f, y - radius * 0.2f, radius * 0.2f, paint)
            canvas.drawCircle(x + radius * 0.4f, y - radius * 0.2f, radius * 0.2f, paint)

            // Нос
            paint.color = Color.argb(255, 255, 105, 180) // Розовый
            canvas.drawCircle(x, y, radius * 0.15f, paint)
        }
    }

    fun checkCollision(mouseX: Float, mouseY: Float, mouseRadius: Float): Boolean {
        val dx = x - mouseX
        val dy = y - mouseY
        val distance = sqrt(dx * dx + dy * dy)
        return distance < (radius + mouseRadius)
    }
}