//
//  Assembly.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

/**
 * An interface that has implementations in simprokmachine-android and simprokcore-android.
 * Should not be used directly.
 */
import kotlinx.coroutines.CoroutineScope

interface Assembly {

    fun start(scope: CoroutineScope)

    companion object
}