package com.example.supermetronome

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import java.io.*
import java.lang.Byte
import java.lang.Short

const val SAMPLE_RATE = 44100
const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT
val BUFFER_SIZE_PLAYING = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

class AudioTrackHandler(val context: Context){
    init {
        writeToAudioTrack()
    }

    val shortSizeInBytes = Short.SIZE / Byte.SIZE
    var bufferSizeInBytes = 18194
    val audioData = ShortArray(bufferSizeInBytes)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH) // defines the type of content being played
        .setUsage(AudioAttributes.USAGE_MEDIA) // defines the purpose of why audio is being played in the app
        .build()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var audioFormat = AudioFormat.Builder()
        .setEncoding(AudioFormat.ENCODING_PCM_8BIT) // we plan on reading byte arrays of data, so use the corresponding encoding
        .setSampleRate(SAMPLE_RATE)
        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .build()

    // TODO: 28.12.2021 invalid mode illegalArgumentException
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        var audioTrack = AudioTrack(
            audioAttributes, audioFormat, bufferSizeInBytes,
            AudioTrack.MODE_STATIC, AudioManager.AUDIO_SESSION_ID_GENERATE
        )


    fun writeToAudioTrack(){
        try {
            val dis = DataInputStream(context.resources.openRawResource(R.raw.metro2))

            var i = 0

            while (dis.available() > 0) {
                audioData[i] = dis.readShort();
                i++;
            }

            audioTrack.write(audioData,0,bufferSizeInBytes)

            dis.close()
        }catch (e: Exception){
            println(e.toString())
            println("Invalid File Path!")
        }
    }
}