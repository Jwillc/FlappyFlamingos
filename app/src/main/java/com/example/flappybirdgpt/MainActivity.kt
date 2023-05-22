package com.example.flappybirdgpt

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

    var birdVelocity = 0f
    val gravity = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this, birdVelocity)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        startGame()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startGame() {
        gameView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val jumpForce = -10f
                    birdVelocity = jumpForce
                    gameView.setBirdVelocity(birdVelocity)
                }
            }
            true
        }

        val random = Random()
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
        val pipeWidth = 200f
        val pipeHeight = 600f // Adjust the pipe height as needed
        val minPipeGap = 150f // Minimum pipe gap size
        val maxPipeGap = 200f // Maximum pipe gap size

        var pipeX = gameView.width.toFloat() // Initial pipe position

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                // Update bird position
                val newBirdY = gameView.birdY + birdVelocity
                birdVelocity += gravity
                gameView.updateBirdPosition(gameView.birdX, newBirdY, birdVelocity)

                // Update pipe position
                pipeX -= 10f // Move the pipe towards the bird (adjust the value as needed)

                if (pipeX + pipeWidth < 0) {
                    // Reset pipe position when it goes off the screen
                    pipeX = gameView.width.toFloat()
                    val pipeGap = random.nextFloat() * (maxPipeGap - minPipeGap) + minPipeGap // Random pipe gap within the specified range
                    val pipeY = random.nextInt((screenHeight - pipeGap - pipeHeight).toInt()).toFloat()

                    gameView.updatePipePosition(pipeX, pipeY, pipeGap, pipeWidth, pipeHeight)
                } else {
                    gameView.updatePipePosition(pipeX, gameView.pipeY, gameView.pipeGap, pipeWidth, pipeHeight)
                }

                handler.postDelayed(this, 16) // Adjust the delay according to your needs
            }
        }

        handler.post(runnable)
    }
}

