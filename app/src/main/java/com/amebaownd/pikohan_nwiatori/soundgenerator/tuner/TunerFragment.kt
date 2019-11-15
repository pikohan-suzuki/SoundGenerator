package com.amebaownd.pikohan_nwiatori.soundgenerator.tuner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amebaownd.pikohan_nwiatori.soundgenerator.databinding.FragmentTunerBinding
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.getViewModelFactory
import android.view.Gravity



class TunerFragment : Fragment() {

    private val viewModel: TunerViewModel by viewModels { getViewModelFactory() }

    private lateinit var fragmentTunerBinding: FragmentTunerBinding
    private var isPermissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTunerBinding = FragmentTunerBinding.inflate(inflater, container, false).apply {
            viewModel = this@TunerFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return fragmentTunerBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val toast = Toast.makeText(
                this.context,
                "Please enable microphone permissions to use this feature.", Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
            isPermissionGranted=false
        } else {
            viewModel.start()
            isPermissionGranted=true
        }
    }

    override fun onPause() {
        super.onPause()
        if(isPermissionGranted)
            viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isPermissionGranted)
            viewModel.onStop()
    }
}