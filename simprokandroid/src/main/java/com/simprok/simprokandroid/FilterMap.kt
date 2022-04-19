//
//  FilterMap.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

sealed interface FilterMap<T> {

    data class Map<T>(val value: T) : FilterMap<T>

    class Filter<T> : FilterMap<T>
}