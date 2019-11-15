package com.amebaownd.pikohan_nwiatori.soundgenerator.sound

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.soundgenerator.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundViewModel() : ViewModel() {

    companion object {
        const val STREAM_TYPE = AudioManager.STREAM_MUSIC
        const val SAMPLE_RATE = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        val BUFFER_SIZE =
            AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        const val MODE = AudioTrack.MODE_STREAM
    }

    private val mAudioTrack =
        AudioTrack(STREAM_TYPE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, MODE)
    private val mSoundGenerator = SoundGenerator(BUFFER_SIZE, SAMPLE_RATE)
    private var isRunning = false

    var isPlaying = MutableLiveData<Boolean>(false)
    var hzProgress = MutableLiveData<Int>(440)


    fun onOnOffButtonClicked() {
        isPlaying.value = !(isPlaying.value ?: true)
    }

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            isRunning = true
            var soundShortArray = ShortArray(BUFFER_SIZE) { i: Int -> 0.toShort() }
            while (isRunning) {
                mAudioTrack.play()

                if (isPlaying.value ?: false) {
//                    viewModelScope.launch {
                    val soundData = mSoundGenerator.getNext(hzProgress.value ?: 0)
//                        val soundShortList = mutableListOf<Short>()
                    for (i in soundData.indices) {
//                            soundShortList.add((dlValue * Short.MAX_VALUE).toShort())
                        soundShortArray[i] = (soundData[i] * Short.MAX_VALUE).toShort()
                    }
//                    }
//                        soundShortArray = soundShortList.toTypedArray().toShortArray()
                } else {
                    soundShortArray = ShortArray(BUFFER_SIZE) { i: Int -> 0.toShort() }
                }
                mAudioTrack.write(
                    soundShortArray,
                    0,
                    BUFFER_SIZE
                )
            }
            mAudioTrack.stop()
        }
    }

    fun onPlusMinusButtonClicked(view: View) {
        when (view.id) {
            R.id.sound_minus_button -> {
                hzProgress.value =
                    if (hzProgress.value!! <= 5)
                        0
                    else
                        hzProgress.value!! - 5
            }
            R.id.sound_plus_button -> {
                hzProgress.value =
                    if (hzProgress.value!! >= 19995)
                        20000
                    else
                        hzProgress.value!! + 5
            }
        }
    }

    fun stopAudioTrack(){
        isRunning=false
        isPlaying.value=false
        mAudioTrack.stop()
    }
    fun flashAudioTrack() {
        isRunning=false
        mAudioTrack.stop()
        mAudioTrack.flush()
    }
}