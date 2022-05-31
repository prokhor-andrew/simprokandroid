//
//  ChildWidgetMachine.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.machines.ChildMachine
import com.simprok.simprokmachine.machines.Machine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class ChildWidgetMachine<Input, Output>
    : WidgetMachine<Input, Output>, ChildMachine<Input, Output> {

    private var privateInputSetter: Handler<Input?>? = null
    var inputSetter: Handler<Input?>?
        get() = privateInputSetter
        set(value) {
            privateInputSetter = value
        }

    private var privateOutputCallback: Handler<Output>? = null

    fun callback(output: Output) {
        privateOutputCallback?.invoke(output)
    }

    override val machine: Machine<Input, Output>
        get() = this

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override fun process(input: Input?, callback: Handler<Output>) {
        privateInputSetter?.invoke(input)
        privateOutputCallback = callback
    }
}