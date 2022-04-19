//
//  BasicWidgetMachine.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import com.simprok.simprokmachine.machines.Machine

internal class BasicWidgetMachine<Input, Output>(
    override val machine: Machine<Input, Output>
) : WidgetMachine<Input, Output>