package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentMetronomeBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.EventObserver
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_metronome.*
import kotlinx.coroutines.*
import java.time.format.SignStyle

class MetronomeFragment : Fragment() {

    private val viewModel: MetronomeViewModel by viewModels { getViewModelFactory() }

    private lateinit var fragmentMetronomeBinding: FragmentMetronomeBinding

    private var isPlaying = false

    private lateinit var animateL2R: ObjectAnimator
    private lateinit var animateR2L: ObjectAnimator

    private var startAnimationTimeStump = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMetronomeBinding =
            FragmentMetronomeBinding.inflate(inflater, container, false).apply {
                viewModel = this@MetronomeFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
            }
        return fragmentMetronomeBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSeekBar()
        onStartTimeChanged()
    }

    override fun onStart() {
        super.onStart()
        onStartMetronome()
    }

    private fun setupSeekBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            metronome_tempo_seekBar.min = 20
        } else {
            viewModel.tempoSeekBarProgress.observe(this, Observer {
                if (it < 20)
                    viewModel.tempoSeekBarProgress.value = 20
            })
        }
    }

    private fun onStartTimeChanged() {
        viewModel.startAnimateTime.observe(this, Observer {
            if(it!=-1L) {
                startAnimationTimeStump = it
                Log.d("startTime", it.toString())
            }else{
                startAnimationTimeStump = System.currentTimeMillis()
            }
        })
    }

    private fun onStartMetronome() {
        viewModel.onStartEvent.observe(this, EventObserver {
            if (it) {
                isPlaying = true
                metronome_tempo_seekBar.isEnabled = false
                animateMetronome()
            } else {
                isPlaying = false
                metronome_tempo_seekBar.isEnabled = true
                try {
                    if (animateL2R.isRunning)
                        animateL2R.cancel()
                    if (animateR2L.isRunning)
                        animateR2L.cancel()
                } catch (e: UninitializedPropertyAccessException) {

                }
            }
        })
    }

    private fun animateMetronome() {
        val windowSize = Point()
        this.activity?.windowManager?.defaultDisplay?.getSize(windowSize)
        val pivotY = PropertyValuesHolder.ofFloat("pivotY", windowSize.y / 4f)
        val angle = (Math.atan(((windowSize.x / 2f) / (windowSize.y / 4f)).toDouble()) * 180f / Math.PI).toFloat()
        val rotateC2R = PropertyValuesHolder.ofFloat("rotation", 0f, angle)
        val rotateL2R = PropertyValuesHolder.ofFloat("rotation", -angle, angle)
        val rotateR2L = PropertyValuesHolder.ofFloat("rotation", angle, -angle)
        val duration = 60000 / (viewModel.tempoSeekBarProgress.value ?: 60).toLong()
        animateL2R =
            ObjectAnimator.ofPropertyValuesHolder(metronome_tempo_view, rotateL2R, pivotY).apply {
                setDuration(duration)
            }
        animateR2L =
            ObjectAnimator.ofPropertyValuesHolder(metronome_tempo_view, rotateR2L, pivotY).apply {
                setDuration(duration)
            }
        val coroutineContext = Job() + Dispatchers.Main
        val scope = CoroutineScope(coroutineContext)

        scope.launch {
            while (isPlaying) {
                var playTime = duration + startAnimationTimeStump - System.currentTimeMillis()
                if(playTime < 0 ) playTime =duration
                animateL2R.duration = playTime
                Log.d("playTime",playTime.toString())
                animateL2R.start()
                scope.async(Dispatchers.IO)  {
                    while(animateL2R.isRunning && isPlaying){
                    }
                }.await()

                if(!isPlaying)
                    animateL2R.cancel()
                playTime = duration + startAnimationTimeStump - System.currentTimeMillis()
                if(playTime < 0 ) playTime =duration
                animateR2L.duration = playTime
                Log.d("playTime",playTime.toString())
                animateR2L.start()
                scope.async(Dispatchers.IO)  {
                    while(animateR2L.isRunning && isPlaying){
                    }
                }.await()

                if(!isPlaying)
                animateR2L.cancel()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isPlaying=false
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        isPlaying=false
        viewModel.onDestroy()
    }


}