package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.R
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentMetronomeBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory

class MetronomeFragment :Fragment(){

    private val viewModel:MetronomeViewModel by viewModels{getViewModelFactory()}

    private lateinit var fragmentMetronomeBinding: FragmentMetronomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        fragmentMetronomeBinding = FragmentMetronomeBinding.inflate(inflater,container,false).apply {
            viewModel = this@MetronomeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentMetronomeBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}