package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.graphics.drawable.Drawable
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amebaownd.pikohan_nwiatori.soundgenerator.MainActivity
import com.amebaownd.pikohan_nwiatori.soundgenerator.R
import com.amebaownd.pikohan_nwiatori.soundgenerator.sound.SoundViewModel
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.Event
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.MyContext
import kotlinx.android.synthetic.main.fragment_metronome.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MetronomeViewModel() : ViewModel() {

    companion object {
        const val STREAM_TYPE = AudioManager.STREAM_MUSIC
        const val SAMPLE_RATE = 44100
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        val BUFFER_SIZE =
            AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        const val MODE = AudioTrack.MODE_STREAM
    }

    private val lampLightDrawable = MyContext.getDrawable(R.drawable.background_lamp_light)
    private val lampDarkDrawable = MyContext.getDrawable(R.drawable.background_lamp_dark)

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
    var rhythm = 1
    var tempoSeekBarProgress = MutableLiveData<Int>(60)
    var isPlaying = MutableLiveData<Boolean>(false)

    private var state = 1

    private var _onStartEvent = MutableLiveData<Event<Boolean>>(Event(false))
    val onStartEvent :LiveData<Event<Boolean>> = _onStartEvent

    private var _lampResId1 =
        MutableLiveData<Drawable>(lampDarkDrawable)
    private var _lampResId2 =
        MutableLiveData<Drawable>(lampDarkDrawable)
    private var _lampResId3 =
        MutableLiveData<Drawable>(lampDarkDrawable)
    private var _lampResId4 =
        MutableLiveData<Drawable>(lampDarkDrawable)

    private var _lampResId5 =
        MutableLiveData<Drawable>(lampDarkDrawable)
    private var _lampResId6 =
        MutableLiveData<Drawable>(lampDarkDrawable)

    val lampResIds: List<MutableLiveData<Drawable>> =
        listOf(_lampResId1, _lampResId2, _lampResId3, _lampResId4, _lampResId5, _lampResId6)

    private var _lampVisibility1 = MutableLiveData<Int>(View.VISIBLE)
    private var _lampVisibility2 = MutableLiveData<Int>(View.GONE)
    private var _lampVisibility3 = MutableLiveData<Int>(View.GONE)
    private var _lampVisibility4 = MutableLiveData<Int>(View.GONE)
    private var _lampVisibility5 = MutableLiveData<Int>(View.GONE)
    private var _lampVisibility6 = MutableLiveData<Int>(View.GONE)
    val lampVisibilities: List<MutableLiveData<Int>> =
        listOf(
            _lampVisibility1,
            _lampVisibility2,
            _lampVisibility3,
            _lampVisibility4,
            _lampVisibility5,
            _lampVisibility6
        )

    private var backgroundRhythmButtonDrawable = MyContext.getDrawable(R.drawable.background_rhythm_button)
    private var backgroundRhythmButtonCheckedDrawable = MyContext.getDrawable(R.drawable.background_rhythm_button_checked)
    private var _rhythmButtonBackground1 = MutableLiveData<Drawable>(backgroundRhythmButtonCheckedDrawable)
    private var _rhythmButtonBackground2 = MutableLiveData<Drawable>(backgroundRhythmButtonDrawable)
    private var _rhythmButtonBackground3 = MutableLiveData<Drawable>(backgroundRhythmButtonDrawable)
    private var _rhythmButtonBackground4 = MutableLiveData<Drawable>(backgroundRhythmButtonDrawable)
    private var _rhythmButtonBackground6 = MutableLiveData<Drawable>(backgroundRhythmButtonDrawable)
    val rhythmButtonBackground = listOf(
        _rhythmButtonBackground1,
        _rhythmButtonBackground2,
        _rhythmButtonBackground3,
        _rhythmButtonBackground4,
        _rhythmButtonBackground6
    )

    fun onStartStopButtonClicked() {
        if (isPlaying.value!!) {
            isPlaying.value = false
            _onStartEvent.value = Event(false)
        } else {
            isPlaying.value = true
            _onStartEvent.value = Event(true)
            rhythmGenerator = RhythmGenerator(SAMPLE_RATE, BUFFER_SIZE).apply {
                this.setRhythm(rhythm)
                this.setTempo(tempoSeekBarProgress.value!!)
            }
            mAudioTrack.play()
            viewModelScope.launch(Dispatchers.IO) {
                while (isPlaying.value ?: false) {
                    val resultData = rhythmGenerator.getNextRhythm()
                    val soundData = resultData.first
                    val timingData = resultData.second

                    timingData.toSortedMap().forEach {
                        if (state != it.key) {
                            state = it.key
                            viewModelScope.launch() {
                                val delayMs = (1000 * it.value / SAMPLE_RATE).toLong()
                                delay(delayMs)
                                Log.d("ssss",System.currentTimeMillis().toString())
                                changeLamp(it.key)
                            }
                        }
                    }
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

    private fun changeLamp(num: Int) {
//        Log.d("lampId",num.toString())
        val beforeId =
            if (num == 0)
                rhythm - 1
            else
                num - 1
        lampResIds[beforeId].postValue(lampDarkDrawable)
        lampResIds[num].postValue(lampLightDrawable)
    }

    private fun setVisibility() {
        for (i in lampVisibilities.indices) {
            lampVisibilities[i].value =
                if (i < rhythm)
                    View.VISIBLE
                else
                    View.GONE
        }
    }

    fun onDestroy() {
        mAudioTrack.stop()
        mAudioTrack.flush()
    }

    fun onButtonClicked(view: View) {
        rhythmButtonBackground[if(rhythm==6)4 else rhythm-1].value = backgroundRhythmButtonDrawable
        when (view.id) {
            R.id.metronome_tempo_button1 -> {
                rhythm = 1
            }
            R.id.metronome_tempo_button2 -> {
                rhythm = 2
            }
            R.id.metronome_tempo_button3 -> {
                rhythm = 3
            }
            R.id.metronome_tempo_button4 -> {
                rhythm = 4
            }
            R.id.metronome_tempo_button6 -> {
                rhythm = 6
            }
        }
        rhythmButtonBackground[if(rhythm==6)4 else rhythm-1].value = backgroundRhythmButtonCheckedDrawable
        setVisibility()
    }
}