//
//  RenderingObject.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.simprok.simprokmachine.api.BiHandler
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.Mapper

/**
 * An object that provides methods for the control of the UI rendering stream.
 */
class RenderingObject<Input, Output> internal constructor(
    private val owner: LifecycleOwner,
    private val liveData: LiveData<Input?>,
    private val callback: Handler<Output>
) {

    /**
     * Applies a behavior to the stream of data for UI rendering depending on the FilterMap object returned from the func().
     * If FilterMap.Map - passes mapped input further.
     * If FilterMap.Filter - ignores the object.
     *
     * @param mapper - function that applies behavior.
     * @return an object that provides methods for further control of the stream.
     */
    fun <ChildInput> filterMapInput(mapper: Mapper<Input?, FilterMap<ChildInput?>>): RenderingObject<ChildInput, Output> {
        return RenderingObject(
            owner,
            liveData.filterMap(mapper),
            callback
        )
    }

    /**
     * Applies a behavior to the stream of data for UI rendering depending on the FilterMap object returned from the func().
     * If FilterMap.Map - passes mapped output further.
     * If FilterMap.Filter - ignores the object.
     *
     * @param mapper - function that applies behavior.
     * @return an object that provides methods for further control of the stream.
     */
    fun <ChildOutput> filterMapOutput(mapper: Mapper<ChildOutput, FilterMap<Output>>): RenderingObject<Input, ChildOutput> {
        val callback: Handler<ChildOutput> = { output ->
            when (val result = mapper(output)) {
                is FilterMap.Map<Output> -> callback(result.value)
                is FilterMap.Filter<Output> -> {}
            }
        }
        return RenderingObject(
            owner,
            liveData,
            callback
        )
    }

    /**
     * Subscribes to the flow that is triggered every time the UI
     * WidgetMachine is updated with new input.
     * @param handler - triggered after subscription and every time the UI WidgetMachine
     * is updated with new input.
     */
    fun render(handler: BiHandler<Input?, Handler<Output>>) {
        liveData.observe(owner) { input ->
            handler(input) { output -> callback(output) }
        }
    }
}
