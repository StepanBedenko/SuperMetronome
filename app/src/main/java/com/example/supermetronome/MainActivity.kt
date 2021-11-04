package com.example.supermetronome

import android.app.appsearch.GlobalSearchSession
import android.content.Context
import android.drm.DrmStore
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.join
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.lang.String.join
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlin.system.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    var isPlaying: Boolean = false
    var bpm: Int = 130
    var bpmToMillis = countBpm(bpm)
    var divisible: Int = 4
    var divisor: Int = 4


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var soundPool: SoundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()


    var metronome: MetronomeLogic = MetronomeLogic(bpmToMillis, divisible, divisor, soundPool)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val minusBtnUp = findViewById<Button>(R.id.minusButtonUp)
        val plusBtnUp = findViewById<Button>(R.id.plusButtonUp)
        val upNum = findViewById<TextView>(R.id.upNum)
        val minusBtnDown = findViewById<Button>(R.id.minusButtonDown)
        val plusBtnDown = findViewById<Button>(R.id.plusButtonDown)
        val downNum = findViewById<TextView>(R.id.downNum)
        val playBtn = findViewById<Button>(R.id.playBtn)
        val bpmPlusBtn = findViewById<Button>(R.id.bpmPlus)
        val bpmMinusBtn = findViewById<Button>(R.id.bpmMinus)
        val bpmView = findViewById<TextView>(R.id.bpmView)



        soundPool.load(applicationContext, R.raw.metro1,1)
        soundPool.load(applicationContext, R.raw.metro2,1)


        upNum.text = divisible.toString()
        downNum.text = divisor.toString()
        bpmView.text = bpm.toString()


        var counterUp = upNum.text.toString().toInt()
        var counterDown = downNum.text.toString().toInt()

        bpmView.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    val num: Int = v.text.toString().toInt()
                    if (num in 1..300){
                        v.text = num.toString()
                        bpm = num
                        bpmToMillis = countBpm(bpm)
                        metronome.bpmMillis = bpmToMillis
                    } else{
                        v.text = bpm.toString()
                    }
                    true
                }
                else -> false
            }
        }

        bpmPlusBtn.setOnClickListener {
            if (bpm < 300) {
                bpm++
                bpmView.text = bpm.toString()
                bpmToMillis = countBpm(bpm)
                metronome.bpmMillis = bpmToMillis
            }
        }

        bpmMinusBtn.setOnClickListener {
            if (bpm > 0) {
                bpm--
                bpmView.text = bpm.toString()
                bpmToMillis = countBpm(bpm)
                metronome.bpmMillis = bpmToMillis
            }

        }

        playBtn.setOnClickListener {
            if (!isPlaying) {
                playBtn.setText(R.string.stop)
                isPlaying = true
                metronome.start()
            }
            else {
                isPlaying = false
                playBtn.setText(R.string.play)
                metronome.stop()
            }
        }

        minusBtnUp.setOnClickListener {
            if (counterUp > 1) {
                counterUp -= 1
                divisible = counterUp
                metronome.divisible = counterUp
            }
            upNum.text = counterUp.toString()
        }

        plusBtnUp.setOnClickListener {
            if (counterUp < 20) {
                counterUp += 1
                divisible = counterUp
                metronome.divisible = counterUp
            }
            upNum.text = counterUp.toString()
        }

        minusBtnDown.setOnClickListener {
            if (counterDown > 1)
                counterDown -= 1
            downNum.text = counterDown.toString()
        }

        plusBtnDown.setOnClickListener {
            if (counterDown < 20)
                counterDown += 1
            downNum.text = counterDown.toString()
        }
    }


    fun countBpm(currentBbm: Int): Long {
        return (60000 / currentBbm).toLong()
    }

    suspend fun startClick(isPlaying: Boolean, bpmToMillis: Long, metro1: MediaPlayer) =
        coroutineScope {
            launch {
                while (isPlaying) {
                    metro1.start()
                    delay(bpmToMillis)
                }
            }
        }
}

