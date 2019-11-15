package com.amebaownd.pikohan_nwiatori.soundgenerator.tuner

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

class TunerViewModel ():ViewModel(){

    companion object {
        const val FFT_SIZE = 4096
        const val SAMPLING_RATE = 44100
        const val FRAME_RATE = 10
        const val ONE_FRAME_DATA_COUNT = SAMPLING_RATE / FRAME_RATE
        const val ONE_FRAME_SIZE_IN_BYTE = ONE_FRAME_DATA_COUNT * 2
        val audioBufferSizeInByte =
            max(
                ONE_FRAME_SIZE_IN_BYTE * 10,
                AudioRecord.getMinBufferSize(
                    SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT))
        val bufSize = AudioRecord.getMinBufferSize(
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT)
    }

    private var dB_baseline = Math.pow(2.0, 15.0) * FFT_SIZE * Math.sqrt(2.0)
    private val mAudioRecord: AudioRecord
    private var fft = FFT4g(FFT_SIZE)
    private var isRecording: Boolean = false
    var resol = SAMPLING_RATE / FFT_SIZE.toDouble()

    var hzText = MutableLiveData<String>()

    init {
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            audioBufferSizeInByte)
    }

    fun start() {
        isRecording=true
        mAudioRecord.startRecording()
        viewModelScope.launch(Dispatchers.IO) {
            val audioDataArray = ShortArray(bufSize)
            while (isRecording) {
                mAudioRecord.read(audioDataArray, 0, bufSize)
                val doubleDataArray = DoubleArray(FFT_SIZE)
                for (i in 0 until bufSize) {
                    doubleDataArray[i] = audioDataArray[i].toDouble()
                }
                fft.rdft(1, doubleDataArray)

                val dbfs = DoubleArray(FFT_SIZE / 2)
                var max_db = -120.0
                var max_i = 0
                var i = 0
                while (i < FFT_SIZE) {
                    dbfs[i / 2] = ((20 * Math.log10(
                        (Math.sqrt(
                            Math
                                .pow(doubleDataArray[i], 2.0) + Math.pow(
                                doubleDataArray[i + 1],
                                2.0
                            )
                        ) / dB_baseline)
                    ))).toInt().toDouble()
                    if (max_db < dbfs[i / 2]) {
                        max_db = dbfs[i / 2]
                        max_i = i / 2
                    }
                    i += 2
                }
                val hz = (resol * max_i).toInt()
                hzText.postValue(hz.toString()+"Hz")
                Log.d("Tuner","Hz:$hz")
            }
            mAudioRecord.stop()
        }
    }

    fun onPause(){
        isRecording=false
        mAudioRecord.stop()
    }

    fun onStop(){
        isRecording=false
        mAudioRecord.stop()
        mAudioRecord.release()
    }
}