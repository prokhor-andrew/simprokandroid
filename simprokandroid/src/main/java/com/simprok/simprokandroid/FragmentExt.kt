//
//  FragmentExt.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.simprok.simprokmachine.api.BiHandler
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.Mapper


/**
 * Applies a behavior to the stream of data for UI rendering depending on the FilterMap object returned from the func().
 * If FilterMap.Map - passes mapped input further.
 * If FilterMap.Filter - ignores the object.
 *
 * @param func - function that applies behavior.
 * @return - an object that provides methods for further control of the stream.
 */
fun <ParentInput, ParentOutput, ChildInput> Fragment.filterMapInput(
    func: Mapper<ParentInput?, FilterMap<ChildInput?>>
): RenderingObject<ChildInput, ParentOutput> {
    val viewModel: MachineViewModel<ParentInput, ParentOutput> by activityViewModels()
    return RenderingObject(
        this,
        viewModel.liveData.filterMap(func)
    ) { output -> viewModel.callback?.invoke(output) }
}


/**
 * Applies a behavior to the stream of data for UI rendering depending on the FilterMap object returned from the func().
 * If FilterMap.Map - passes mapped output further.
 * If FilterMap.Filter - ignores the object.
 *
 * @param func - function that applies behavior.
 * @return an object that provides methods for further control of the stream.
 */
fun <ParentInput, ParentOutput, ChildOutput> Fragment.filterMapOutput(
    func: Mapper<ChildOutput, FilterMap<ParentOutput>>
): RenderingObject<ParentInput, ChildOutput> {
    val viewModel: MachineViewModel<ParentInput, ParentOutput> by activityViewModels()
    val callback: Handler<ChildOutput> = { output ->
        when (val result = func(output)) {
            is FilterMap.Map<ParentOutput> -> viewModel.callback?.invoke(result.value)
            is FilterMap.Filter<ParentOutput> -> {}
        }
    }
    return RenderingObject(
        this,
        viewModel.liveData,
        callback
    )
}

/**
 * Subscribes to the flow that is triggered every time the
 * UI WidgetMachine is updated with new input.
 * @param func - triggered after subscription and every time the UI
 * WidgetMachine is updated with new input.
 */
fun <FragmentInput, FragmentOutput> Fragment.render(func: BiHandler<FragmentInput?, Handler<FragmentOutput>>) {
    val viewModel: MachineViewModel<FragmentInput, FragmentOutput> by activityViewModels()
    viewModel.liveData.observe(this) { input ->
        func(input) { output ->
            viewModel.callback?.invoke(output)
        }
    }
}

internal fun <T, R> LiveData<T>.filterMap(predicate: Mapper<T, FilterMap<R>>): LiveData<R> {
    val mediatorLiveData: MediatorLiveData<R> = MediatorLiveData()
    mediatorLiveData.addSource(this) {
        when (val result = predicate(it)) {
            is FilterMap.Filter<R> -> {}
            is FilterMap.Map<R> -> mediatorLiveData.value = result.value
        }
    }
    return mediatorLiveData
}