package com.example.flappybirdgpt

import android.content.Context
import android.graphics.Paint
import android.graphics.*
import android.view.View

class GameView(context: Context, private var birdVelocity: Float) : View(context) {
    private val birdPaint: Paint = Paint()
    private val pipePaint: Paint = Paint()

    private var birdBitmap1: Bitmap? = null
    private var birdBitmap2: Bitmap? = null

    private var palmBitmap: Bitmap? = null
    private var palmRepeatableBitmap: Bitmap? = null

    // Constants
    private val birdRadius = 30f

    init {
        birdBitmap1 = BitmapFactory.decodeResource(resources, R.drawable.bird_1)
        birdBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.bird_2)

        // Scale the bitmaps once
        birdBitmap1 = Bitmap.createScaledBitmap(birdBitmap1!!, 150, 150, false)
        birdBitmap2 = Bitmap.createScaledBitmap(birdBitmap2!!, 150, 150, false)

        // Load the palm and palm_repeatable bitmaps
        palmBitmap = BitmapFactory.decodeResource(resources, R.drawable.palm)
        palmRepeatableBitmap = BitmapFactory.decodeResource(resources, R.drawable.palm_repeatable)
    }

    var birdX: Float = 100f
    var birdY: Float = 0f
    private var pipeX: Float = 0f
    var pipeY: Float = 0f
    var pipeGap: Float = 0f
    private var pipeWidth: Float = 0f
    private var pipeHeight: Float = 0f

    private var isGameOver: Boolean = false

    init {
        birdPaint.color = Color.YELLOW
        pipePaint.color = Color.GREEN
        pipePaint.style = Paint.Style.FILL
    }

    fun setBirdVelocity(velocity: Float) {
        birdVelocity = velocity
    }

    fun updateBirdPosition(x: Float, y: Float, velocity: Float) {
        birdX = x
        birdY = y
        birdVelocity = velocity
        postInvalidateOnAnimation() // Redraw the view
    }

    fun updatePipePosition(x: Float, y: Float, gap: Float, width: Float, height: Float) {
        pipeX = x
        pipeY = y
        pipeGap = gap
        pipeWidth = width
        pipeHeight = height
        postInvalidateOnAnimation() // Redraw the view
    }

    private fun checkCollision() {
        val lowerPipeY = pipeY + pipeGap + pipeHeight
        if (birdY < pipeY || birdY > lowerPipeY || birdX + birdRadius > pipeX && birdX - birdRadius < pipeX + pipeWidth) {
            setGameOver()
        }
    }

    private fun setGameOver() {
        isGameOver = true
        postInvalidateOnAnimation() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the bird
        val birdBitmap = if (birdVelocity >= 0) birdBitmap1 else birdBitmap2
        birdBitmap?.let {
            canvas.drawBitmap(it, birdX - birdRadius, birdY - birdRadius, null)
        }

        // Calculate the upper and lower pipe positions
        val upperPipeBottom = pipeY - pipeGap
        val lowerPipeTop = pipeY + pipeGap

        // Draw the upper pipe
        canvas.drawRect(pipeX, 0f, pipeX + pipeWidth, upperPipeBottom, pipePaint)
        // Draw the lower pipe
        canvas.drawRect(pipeX, lowerPipeTop, pipeX + pipeWidth, height.toFloat(), pipePaint)

        // Check for collision
        checkCollision()

        // Game over message
        if (isGameOver) {
            val gameOverTextPaint = Paint()
            gameOverTextPaint.color = Color.RED
            gameOverTextPaint.textSize = 80f
            gameOverTextPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("Game Over", width / 2f, height / 2f, gameOverTextPaint)
        }
    }

}

