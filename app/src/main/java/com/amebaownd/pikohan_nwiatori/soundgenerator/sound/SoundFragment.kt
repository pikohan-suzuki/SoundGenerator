package com.amebaownd.pikohan_nwiatori.soundgenerator.sound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentSoundBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_sound.*
import java.lang.Math.round

class SoundFragment : Fragment() {

    private val viewModel: SoundViewModel by viewModels { getViewModelFactory() }

    private lateinit var fragmentSoundBinding: FragmentSoundBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSoundBinding = FragmentSoundBinding.inflate(inflater, container, false).apply {
            viewModel = this@SoundFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentSoundBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSeekBar()
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }

    private fun setupSeekBar() {
        soundGenerator_hz_seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                val hz = round(51.62f * Math.exp(0.00109 * progress.toDouble()) - 51.62).toInt() * 5
                viewModel.hzProgress.value =
                    if (hz > 20000)
                        20000
                    else
                        hz
//                    =51.62*EXP(0.00109*B2)-51.62
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopAudioTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.flashAudioTrack()
    }
}