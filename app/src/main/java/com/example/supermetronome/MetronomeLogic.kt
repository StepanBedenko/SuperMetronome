package com.example.supermetronome

import android.media.MediaPlayer
import android.media.SoundPool
import android.provider.MediaStore
import kotlinx.coroutines.*

class MetronomeLogic(
    var bpmMillis: Long, var divisible: Int, var divisor: Int, val soundPool: SoundPool
)
{
    var isPlaying = false
    var job: Job? = null

    fun start(){
        job = GlobalScope.launch(Dispatchers.Default) {
            while (true) {
                for (n in 1..divisible) {
                    if (n == 1) {
                        soundPool.play(1,1F,1F,1,0,1F)
                    } else {
                        soundPool.play(2,1F,1F,1,0,1F)
                    }
                    delay(bpmMillis)
                }
            }
        }
    }

    fun stop(){
        job?.cancel()
    }

}