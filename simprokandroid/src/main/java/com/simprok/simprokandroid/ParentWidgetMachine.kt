//
//  ParentWidgetMachine.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import com.simprok.simprokmachine.machines.Machine

/**
 * An interface that describes an intermediate widget machine that passes
 * input from its parent to the child, and its output from the child to the parent.
 */
interface ParentWidgetMachine<Input, Output> : WidgetMachine<Input, Output> {

    override val machine: Machine<Input, Output>
        get() = child.machine

    /**
     * A child widget machine that receives input that comes
     * from the parent widget machine, and sends output.
     */
    val child: WidgetMachine<Input, Output>
}