package com.stuart.palma.cazarpatos

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var textViewUser: TextView
    private lateinit var textViewCounter: TextView
    private lateinit var textViewTime: TextView
    private lateinit var imageViewDuck: ImageView
    private lateinit var soundPool: SoundPool

    private val handler = Handler(Looper.getMainLooper())
    private var counter = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private var soundId: Int = 0
    private var isLoaded = false
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        textViewUser = findViewById(R.id.textViewUser)
        textViewCounter = findViewById(R.id.textViewCounter)
        textViewTime = findViewById(R.id.textViewTime)
        imageViewDuck = findViewById(R.id.imageViewDuck)

        // Obtener usuario desde LoginActivity
        val usuario = intent.getStringExtra(EXTRA_LOGIN) ?: "Unknown"
        textViewUser.text = usuario

        // Inicialización de pantalla y cuenta regresiva
        initializeScreen()
        initializeCountdown()

        // Configuración del SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(10)
            .build()

        soundId = soundPool.load(this, R.raw.gunshot, 1)
        soundPool.setOnLoadCompleteListener { _, _, _ -> isLoaded = true }

        // Evento de clic en el pato
        imageViewDuck.setOnClickListener {
            if (gameOver) return@setOnClickListener

            counter++
            if (isLoaded) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            }

            textViewCounter.text = counter.toString()
            imageViewDuck.setImageResource(R.drawable.duck_clicked)

            handler.postDelayed({
                imageViewDuck.setImageResource(R.drawable.duck)
            }, 500)

            moveDuckRandomly()
        }
    }

    private fun initializeScreen() {
        val display = resources.displayMetrics
        screenWidth = display.widthPixels
        screenHeight = display.heightPixels
    }

    private fun moveDuckRandomly() {
        val halfWidth = imageViewDuck.width / 2
        val maxX = screenWidth - imageViewDuck.width
        val maxY = screenHeight - imageViewDuck.height

        val randomX = Random.nextInt(0, maxX - halfWidth + 1)
        val randomY = Random.nextInt(0, maxY - halfWidth + 1)

        imageViewDuck.animate()
            .x(randomX.toFloat())
            .y(randomY.toFloat())
            .setDuration(300)
            .start()
    }

    private val countDownTimer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished / 1000
            textViewTime.text = "${secondsRemaining}s"
        }

        override fun onFinish() {
            textViewTime.text = "0s"
            gameOver = true
            showGameOverDialog()
        }
    }

    private fun initializeCountdown() {
        countDownTimer.start()
    }

    private fun showGameOverDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dialog_message_congratulations, counter))
            .setTitle(getString(R.string.dialog_title_game_end))
            .setPositiveButton(getString(R.string.button_restart)) { _, _ ->
                restartGame()
            }
            .setNegativeButton(getString(R.string.button_close)) { _, _ -> }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun restartGame() {
        counter = 0
        gameOver = false
        countDownTimer.cancel()
        textViewCounter.text = counter.toString()
        moveDuckRandomly()
        initializeCountdown()
    }

    override fun onStop() {
        Log.w(EXTRA_LOGIN, "Play canceled")
        countDownTimer.cancel()
        textViewTime.text = "0s"
        gameOver = true
        soundPool.stop(soundId)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    companion object {
        const val EXTRA_LOGIN = "EXTRA_LOGIN"
    }
}
