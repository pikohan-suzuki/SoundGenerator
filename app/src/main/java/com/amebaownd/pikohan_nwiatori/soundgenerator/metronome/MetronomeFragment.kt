package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentMetronomeBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.Event
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.EventObserver
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_metronome.*
import kotlinx.coroutines.*
import java.lang.NullPointerException

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

        onStartMetronome()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun onStartMetronome() {
        viewModel.onStartEvent.observe(this, EventObserver {
            if (it) {
                isPlaying = true
                startAnimationTimeStump=System.currentTimeMillis()
                animateMetronome()
            } else {
                isPlaying = false
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
        val viewSize = Point(metronome_tempo_view.width, metronome_tempo_view.height)
        val viewPosition = Point(metronome_tempo_view.x.toInt(), metronome_tempo_view.y.toInt())
        this.activity?.windowManager?.defaultDisplay?.getSize(windowSize)
//        val pivotX = PropertyValuesHolder.ofFloat("pivotX",)
        val pivotY = PropertyValuesHolder.ofFloat("pivotY", windowSize.y / 4f)
        val angle =
            (Math.atan(((windowSize.x / 2f) / (windowSize.y / 4f)).toDouble()) * 180f / Math.PI).toFloat()

        val rotateC2R = PropertyValuesHolder.ofFloat("rotation", 0f, angle)
        val rotateL2R = PropertyValuesHolder.ofFloat("rotation", -angle, angle)
        val rotateR2L = PropertyValuesHolder.ofFloat("rotation", angle, -angle)

        val duration = 60000 / (viewModel.tempoSeekBarProgress.value ?: 60).toLong()
        val animateC2R =
            ObjectAnimator.ofPropertyValuesHolder(metronome_tempo_view, rotateC2R, pivotY).apply {
                setDuration(duration)
            }
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
                val startTime = startAnimationTimeStump
                animateL2R.start()
                delay(duration)
                if(startTime!=startAnimationTimeStump)
                    break
                animateR2L.start()
                delay(duration)
            }
        }
    }


}