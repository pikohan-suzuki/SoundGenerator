package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amebaownd.pikohan_nwiatori.soundgenerator.R

class MetronomeFragment :Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_metronome,container,false)
    }
}