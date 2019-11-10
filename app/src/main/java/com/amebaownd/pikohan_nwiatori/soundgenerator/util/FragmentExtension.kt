package com.amebaownd.pikohan_nwiatori.soundgenerator.util

import androidx.fragment.app.Fragment
import com.amebaownd.pikohan_nwiatori.soundgenerator.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
//    val userListRepository =
//        ServiceLoader.provideRepository(requireContext())
    return ViewModelFactory()
}