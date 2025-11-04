package com.example.cat_and_mise.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.cat_and_mise.game_objects.*
import kotlin.random.Random

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private lateinit var playerMouse: Mouse
    private val cheeses = mutableListOf<Cheese>()
    private val cats = mutableListOf<Cat>()
    private var targetX = 0f
    private var targetY = 0f
    private var score = 0
    private var isGameRunning = false
    private var gameOver = false
    var testMode: Boolean = false
    var onScoreUpdate: ((Int) -> Unit)? = null
    var onGameOver: ((Int) -> Unit)? = null
    var onCheeseEaten: (() -> Unit)? = null
    private var mouseBitmap: Bitmap? = null
    private var cheeseBitmap: Bitmap? = null
    private var catBitmap: Bitmap? = null

    fun setPlayerMouse(mouse: Mouse) {
        this.playerMouse = mouse
        if (width > 0 && height > 0) {
            initGame(width, height)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::playerMouse.isInitialized && !isGameRunning) {
            initGame(w, h)
        }
    }

    private fun initGame(width: Int, height: Int) {
        val cheeseRadius = playerMouse.radius * 0.5f
        val catRadius = playerMouse.radius * 1.5f

        mouseBitmap = createCircleBitmap(playerMouse.radius.toInt(), Color.GRAY)
        cheeseBitmap = createCircleBitmap(cheeseRadius.toInt(), Color.YELLOW)
        catBitmap = createCircleBitmap(catRadius.toInt(), Color.RED)

        isGameRunning = true
        gameOver = false
        score = 0
        onScoreUpdate?.invoke(score)
    }

    private fun createCircleBitmap(radius: Int, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        return bitmap
    }

    private fun spawnCheese(width: Int, height: Int, radius: Float) {
        val x = Random.nextFloat() * (width - radius * 2) + radius
        val y = Random.nextFloat() * (height - radius * 2) + radius
        cheeses.add(Cheese(x, y, radius, cheeseBitmap))
    }

    private fun spawnCat(width: Int, height: Int, radius: Float) {
        val x = Random.nextFloat() * (width - radius * 2) + radius
        val y = Random.nextFloat() * (height - radius * 2) + radius
        // Скорость кошки зависит от скорости мыши
        val catSpeed = playerMouse.getGameSpeed() * 0.6f
        cats.add(Cat(x, y, radius, catBitmap, speed = catSpeed))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                targetX = event.x
                targetY = event.y
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun update() {
        if (!isGameRunning || gameOver) return
        if (!::playerMouse.isInitialized) return

        val width = width
        val height = height

        if (playerMouse.x == 0f && playerMouse.y == 0f) {
            playerMouse.x = width / 2f
            playerMouse.y = height / 2f
            targetX = playerMouse.x
            targetY = playerMouse.y
        }

        playerMouse.update(targetX, targetY)

        // Обновляем кошек
        cats.forEach { cat ->
            cat.update(width, height)

            // Проверяем столкновение с кошкой
            if (cat.checkCollision(playerMouse.x, playerMouse.y, playerMouse.radius)) {
                endGame()
                return
            }
        }

        // Собираем сыры
        val collectedCheeses = mutableListOf<Cheese>()

        for (cheese in cheeses) {
            if (cheese.isVisible && cheese.checkCollision(playerMouse.x, playerMouse.y, playerMouse.radius)) {
                cheese.isVisible = false
                score += 10
                onScoreUpdate?.invoke(score)
                onCheeseEaten?.invoke()
                collectedCheeses.add(cheese)
            }
        }

        // Удаляем собранные сыры
        cheeses.removeAll(collectedCheeses)

        // Спавним новые сыры вместо собранных
        if (collectedCheeses.isNotEmpty()) {
            repeat(collectedCheeses.size) {
                spawnCheese(width, height, cheeseBitmap?.width?.toFloat() ?: 20f)
            }
        }

        if (Random.nextDouble() < 0.01 && cheeses.size < 3) {
            spawnCheese(width, height, cheeseBitmap?.width?.toFloat() ?: 20f)
        }

        val targetCatCount = when (playerMouse.getGameSpeed()) {
            5 -> 5   // Легкий
            8 -> 7   // Средний
            12 -> 10  // Сложный
            else -> 5
        }

        if (cats.size < targetCatCount && Random.nextDouble() < 0.03) {
            spawnCat(width, height, playerMouse.radius * 1.2f)
        }
        invalidate()
    }

    private fun endGame() {
        gameOver = true
        isGameRunning = false
        onGameOver?.invoke(score)
        invalidate()
    }

    fun pauseGame() {
        isGameRunning = false
    }

    fun resumeGame() {
        if (!gameOver) {
            isGameRunning = true
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)
        cheeses.forEach { it.draw(canvas, paint) }
        cats.forEach { it.draw(canvas, paint) }
        playerMouse.draw(canvas, paint)
    }
}