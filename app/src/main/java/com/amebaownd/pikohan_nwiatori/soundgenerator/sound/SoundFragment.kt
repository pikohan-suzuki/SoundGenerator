package com.amebaownd.pikohan_nwiatori.soundgenerator.sound

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentSoundBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory

class SoundFragment :Fragment(){

    private val viewModel : SoundViewModel by viewModels{getViewModelFactory()}

    private lateinit var fragmentSoundBinding: FragmentSoundBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSoundBinding = FragmentSoundBinding.inflate(inflater,container,false).apply {
            viewModel = this@SoundFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentSoundBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.flashAudioTrack()
    }
}