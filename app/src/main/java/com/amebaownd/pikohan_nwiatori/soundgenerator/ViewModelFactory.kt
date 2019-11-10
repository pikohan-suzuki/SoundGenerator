package com.amebaownd.pikohan_nwiatori.soundgenerator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amebaownd.pikohan_nwiatori.soundgenerator.metronome.MetronomeViewModel
import com.amebaownd.pikohan_nwiatori.soundgenerator.sound.SoundViewModel
import com.amebaownd.pikohan_nwiatori.soundgenerator.tuner.TunerViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory (
    //private val ideaMemoRepository: IdeaMemoRepository
): ViewModelProvider.NewInstanceFactory(){

    override fun <T: ViewModel> create(modelClass:Class<T>): T {
        val t = with(modelClass) {
            when {
                isAssignableFrom(SoundViewModel::class.java) ->
                    SoundViewModel()
                isAssignableFrom(TunerViewModel::class.java) ->
                    TunerViewModel()
                isAssignableFrom(MetronomeViewModel::class.java) ->
                    MetronomeViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModelClass $modelClass")
            }
        } as T
        return t
    }
}