//
//  ActivityExt.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.simprok.simprokmachine.api.BiHandler
import com.simprok.simprokmachine.api.Handler
import com.simprok.simprokmachine.api.Mapper

/**
 * Provides a method for rendering UI as well as a method to assemble core's layers.
 * @param renderer - a method that renders UI.
 * @param assemble - a method that assembles layers. Accepts WidgetMachine instance
 * that transfers all input from the Core to the renderer and
 * all output from the renderer to the Core
 */
fun <ActivityInput, ActivityOutput> AppCompatActivity.startWithRenderer(
    renderer: BiHandler<ActivityInput?, Handler<ActivityOutput>>,
    assemble: Mapper<WidgetMachine<ActivityInput, ActivityOutput>, Assembly>
) {
    val machine = ChildWidgetMachine<ActivityInput, ActivityOutput>()
    val assembly = assemble(machine)
    val viewModel: MachineViewModel<ActivityInput, ActivityOutput> by viewModels {
        MachineViewModelFactory<ActivityInput, ActivityOutput>(assembly)
    }

    machine.inputSetter = {
        viewModel.liveData.value = it
    }

    viewModel.callback = {
        machine.callback(it)
    }

    viewModel.liveData.observe(this) { input ->
        renderer(input) { output -> viewModel.callback?.invoke(output) }
    }
}

/**
 * Provides a method to assemble core's layers.
 * @param assemble: a method that assembles layers.
 */
fun <ActivityInput, ActivityOutput> AppCompatActivity.start(
    assemble: Mapper<WidgetMachine<ActivityInput, ActivityOutput>, Assembly>
) {
    startWithRenderer(
        renderer = { _, _ -> },
        assemble = assemble
    )
}