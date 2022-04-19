//
//  MachineViewModel.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.simprok.simprokmachine.api.Handler

internal class MachineViewModel<State, Event>(assembly: Assembly) : ViewModel() {

    init {
        assembly.start(viewModelScope)
    }

    val liveData: MutableLiveData<State?> by lazy { MutableLiveData() }

    var callback: Handler<Event>? = null
}

@Suppress("UNCHECKED_CAST")
internal class MachineViewModelFactory<State, Event>(
    private val assembly: Assembly
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MachineViewModel::class.java)) {
            MachineViewModel<State, Event>(assembly) as T
        } else {
            throw RuntimeException("Unknown ViewModel")
        }
    }
}