package com.amebaownd.pikohan_nwiatori.soundgenerator.tuner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.R
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentTunerBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory

class TunerFragment :Fragment(){

    private val viewModel :TunerViewModel by viewModels{getViewModelFactory()}

    private lateinit var fragmentTunerBinding:FragmentTunerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTunerBinding = FragmentTunerBinding.inflate(inflater,container,false).apply {
            viewModel = this@TunerFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentTunerBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start()
    }
}