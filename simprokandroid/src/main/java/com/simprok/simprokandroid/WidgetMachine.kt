//
//  WidgetMachine.kt
//  simprokandroid
//
//  Created by Andrey Prokhorenko on 12.03.2022.
//  Copyright (c) 2022 simprok. All rights reserved.

package com.simprok.simprokandroid

import com.simprok.simprokmachine.api.*
import com.simprok.simprokmachine.machines.Machine

/**
 * A general interface that describes a type that represents a
 * machine object which connects to AppCompatActivity.
 * Exists for implementation purposes.
 */
sealed interface WidgetMachine<Input, Output> {

    val machine: Machine<Input, Output>

    companion object
}

/**
 * The same behavior as in Machine.inward() but returns a WidgetMachine object.
 * @param mapper - the same as in Machine.inward().
 * @return the same as in Machine.inward().
 */
fun <ParentInput, ChildInput, Output> WidgetMachine<ChildInput, Output>.inward(
    mapper: Mapper<ParentInput, Ward<ChildInput>>
): WidgetMachine<ParentInput, Output> = BasicWidgetMachine(machine.inward(mapper))

/**
 * The same behavior as in Machine.inward() but returns a WidgetMachine object.
 * @param mapper - the same as in Machine.inward().
 * @return the same as in Machine.inward().
 */
fun <ParentOutput, ChildOutput, Input> WidgetMachine<Input, ChildOutput>.outward(
    mapper: Mapper<ChildOutput, Ward<ParentOutput>>
): WidgetMachine<Input, ParentOutput> = BasicWidgetMachine(machine.outward(mapper))

/**
 * The same behavior as in Machine.redirect() but returns a WidgetMachine object.
 * @param mapper - the same as in Machine.redirect().
 * @return the same as in redirect.inward().
 */
fun <Input, Output> WidgetMachine<Input, Output>.redirect(
    mapper: Mapper<Output, Direction<Input>>
): WidgetMachine<Input, Output> = BasicWidgetMachine(machine.redirect(mapper))

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - set of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> WidgetMachine.Companion.mergeSet(
    main: WidgetMachine<Input, Output>,
    secondary: Set<Machine<Input, Output>>
): WidgetMachine<Input, Output> = BasicWidgetMachine(mergeSet(secondary.plus(main.machine)))

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - array of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> merge(
    main: WidgetMachine<Input, Output>,
    vararg secondary: Machine<Input, Output>
): WidgetMachine<Input, Output> = mergeSet(main, secondary.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - array of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> mergeArray(
    main: WidgetMachine<Input, Output>,
    secondary: Array<Machine<Input, Output>>
): WidgetMachine<Input, Output> = mergeSet(main, secondary.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - list of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> mergeList(
    main: WidgetMachine<Input, Output>,
    secondary: List<Machine<Input, Output>>
): WidgetMachine<Input, Output> = mergeSet(main, secondary.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - set of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> mergeSet(
    main: WidgetMachine<Input, Output>,
    secondary: Set<Machine<Input, Output>>
): WidgetMachine<Input, Output> = main.mergeWithSet(secondary)

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set
 * as well as every output of every child is passed into the resulting machine.
 *
 * @param machines - array of machines that are merged with the main machine.
 * @return resulting machine.
 */
fun <Input, Output> WidgetMachine<Input, Output>.mergeWith(
    vararg machines: Machine<Input, Output>
): WidgetMachine<Input, Output> = mergeWithSet(machines.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set
 * as well as every output of every child is passed into the resulting machine.
 *
 * @param machines - array of machines that are merged with the main machine.
 * @return resulting machine.
 */
fun <Input, Output> WidgetMachine<Input, Output>.mergeWithArray(
    machines: Array<Machine<Input, Output>>
): WidgetMachine<Input, Output> = mergeWithSet(machines.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set
 * as well as every output of every child is passed into the resulting machine.
 *
 * @param machines - list of machines that are merged with the main machine.
 * @return resulting machine.
 */
fun <Input, Output> WidgetMachine<Input, Output>.mergeWithList(
    machines: List<Machine<Input, Output>>
): WidgetMachine<Input, Output> = mergeWithSet(machines.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set
 * as well as every output of every child is passed into the resulting machine.
 *
 * @param machines - set of machines that are merged with the main machine.
 * @return resulting machine.
 */
fun <Input, Output> WidgetMachine<Input, Output>.mergeWithSet(
    machines: Set<Machine<Input, Output>>
): WidgetMachine<Input, Output> = WidgetMachine.mergeSet(this, machines)

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - array of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> WidgetMachine.Companion.merge(
    main: WidgetMachine<Input, Output>,
    vararg secondary: Machine<Input, Output>
): WidgetMachine<Input, Output> = WidgetMachine.mergeSet(main, secondary.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - array of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> WidgetMachine.Companion.mergeArray(
    main: WidgetMachine<Input, Output>,
    secondary: Array<Machine<Input, Output>>
): WidgetMachine<Input, Output> = WidgetMachine.mergeSet(main, secondary.toSet())

/**
 * Creates a WidgetMachine instance with a specific behavior applied.
 * Every input of the resulting machine is passed into every child from the set as well
 * as every output of every child is passed into the resulting machine.
 * @param main - the main machine.
 * @param secondary - list of machines that are merged with the main machine.
 * @return - resulting machine.
 */
fun <Input, Output> WidgetMachine.Companion.mergeList(
    main: WidgetMachine<Input, Output>,
    secondary: List<Machine<Input, Output>>
): WidgetMachine<Input, Output> = WidgetMachine.mergeSet(main, secondary.toSet())

