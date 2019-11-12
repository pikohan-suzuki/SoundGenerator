package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.soundgenerator.sound.SoundViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MetronomeViewModel ():ViewModel(){

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
        AudioTrack(
            STREAM_TYPE,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            BUFFER_SIZE,
            MODE
        )
    private lateinit var rhythmGenerator: RhythmGenerator
    private var rhythm = 1
    var tempoSeekBarProgress = MutableLiveData<Int>(60)
    var isPlaying = MutableLiveData<Boolean>(false)
    private var stopFlg = false

    fun onStartStopButtonClicked(){
        if(isPlaying.value!!){
            isPlaying.value=false
        }else {
            isPlaying.value=true
            rhythmGenerator = RhythmGenerator(SAMPLE_RATE, BUFFER_SIZE).apply {
                this.setRhythm(rhythm)
                this.setTempo(tempoSeekBarProgress.value!!)
            }
            mAudioTrack.play()
            viewModelScope.launch(Dispatchers.IO) {
                while (isPlaying.value ?: false) {
                    val soundData = rhythmGenerator.getNextRhythm()
                    val shortArray = ShortArray(BUFFER_SIZE) { i ->
                        (soundData[i] * Short.MAX_VALUE).toShort()
                    }
                    mAudioTrack.write(
                        shortArray,
                        0,
                        SoundViewModel.BUFFER_SIZE
                    )
                }
                mAudioTrack.stop()
            }
        }
    }

    fun onStart(){

    }

    fun onDestroy(){
        mAudioTrack.flush()
    }

    fun onClickRhythmOff(){
        rhythm=1
    }

    fun onClickRhythm2(){
        rhythm = 2
    }

    fun onClickRhythm3(){
        rhythm = 3
    }

    fun onClickRhythm4(){
        rhythm=4
    }

    fun onClickRhythm6(){
        rhythm=6
    }
}