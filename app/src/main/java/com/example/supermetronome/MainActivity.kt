package com.example.supermetronome


import android.R.attr
import android.media.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import android.R.attr.track
import android.content.Context
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream


const val DEFAULT_BPM = 60
const val DEFAULT_DIVISIBLE = 4
const val DEFAULT_DIVISOR = 4
const val MAX_BPM = 300
const val MIN_BPM = 20
const val MAX_DIVISIBLE = 20
const val MIN_DIVISIBLE = 1
const val  MAX_DIVISOR = 20
const val MIN_DIVISOR = 1

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var bpmMinusImageButton: ImageButton
    lateinit var bpmTextView: TextView
    lateinit var bpmPlusImageButton: ImageButton

    lateinit var dividedMinusImageButton: ImageButton
    lateinit var dividedTextView: TextView
    lateinit var dividedPlusImageButton: ImageButton

    lateinit var divisorMinusImageButton: ImageButton
    lateinit var divisorTextView: TextView
    lateinit var divisorPlusImageButton: ImageButton

    lateinit var playButton: Button

    lateinit var audioTrackHandler: AudioTrackHandler
    lateinit var audioTrack: AudioTrack

    //TODO replace all "divided" with "divisible"
    var isPlaying: Boolean = false
    var bpm: Int = DEFAULT_BPM
    var divisible: Int = DEFAULT_DIVISIBLE
    var divisor: Int = DEFAULT_DIVISOR
    var beatDelay: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        createAudioHandler()
    }

    //TODO rename bpn to pbm. crash
    private fun initViews() {

        bpmMinusImageButton = findViewById(R.id.bpnMinusImageButton)
        bpmTextView = findViewById(R.id.bpmTextView)
        bpmPlusImageButton = findViewById(R.id.bpmPlusImageButton)

        dividedMinusImageButton = findViewById(R.id.dividedMinusImageButton)
        dividedTextView = findViewById(R.id.dividedTextView)
        dividedPlusImageButton = findViewById(R.id.dividedPlusImageButton)

        divisorMinusImageButton = findViewById(R.id.divisorMinusImageButton)
        divisorTextView = findViewById(R.id.divisorTextView)
        divisorPlusImageButton = findViewById(R.id.divisorPlusImageButton)

        playButton = findViewById(R.id.playButton)

        bpmMinusImageButton.setOnClickListener(this)
        bpmPlusImageButton.setOnClickListener(this)

        dividedMinusImageButton.setOnClickListener(this)
        dividedPlusImageButton.setOnClickListener(this)

        divisorMinusImageButton.setOnClickListener(this)
        divisorPlusImageButton.setOnClickListener(this)

        playButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bpnMinusImageButton -> {
                if (bpm > MIN_BPM){
                    bpm--
                    bpmTextView.text = bpm.toString()
                    countBpm()
                }
            }
            R.id.bpmPlusImageButton -> {
                if (bpm < MAX_BPM){
                    bpm++
                    bpmTextView.text = bpm.toString()
                    countBpm()
                }
            }

            R.id.dividedMinusImageButton -> {
                if (divisible > MIN_DIVISIBLE) {
                    divisible--
                    dividedTextView.text = divisible.toString()
                }
            }
            R.id.dividedPlusImageButton -> {
                if (divisible < MAX_DIVISIBLE) {
                    divisible++
                    dividedTextView.text = divisible.toString()
                }
            }

            R.id.divisorMinusImageButton -> {
                if(divisor > MIN_DIVISOR){
                    divisor--
                    divisorTextView.text = divisor.toString()
                }
            }
            R.id.divisorPlusImageButton -> {
                if(divisor < MAX_DIVISOR){
                    divisor++
                    divisorTextView.text = divisor.toString()
                }
            }

            R.id.playButton -> {
                when(isPlaying){
                    false -> {
                        isPlaying = true
                        playButton.text = "Stop"
                        audioTrackHandler.audioTrack.play()
                    }
                    true -> {
                        isPlaying = false
                        playButton.text = "Play"
                        audioTrack.stop()
                    }
                }
            }
            }
        }

    private fun countBpm() {
        beatDelay = (60000 / bpm).toLong()
    }

    fun createAudioHandler(){
        audioTrackHandler = AudioTrackHandler(this)
        audioTrack = audioTrackHandler.audioTrack
    }
}

