package at.ski.noophaestus.forge.casting.actions.display.arithmetic

/**
 * Copyright (c) 2025 Miyu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.nextPositiveIntUnderInclusive
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import net.minecraft.network.chat.Component
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object DisplayArithmetic : Arithmetic {
    val implementations: HashMap<HexPattern, Operator> = HashMap()
    init {
        implementations[Arithmetic.ADD] = twoDisplayIntoIota { a, b -> a.modifyRootBuilder { it.append(b.getRoot()) } }
        implementations[Arithmetic.MUL] = displayNumberIntoIota { display, number -> display.modifyRootString { it.repeat(number.roundToInt().coerceAtLeast(0)) }}
        implementations[Arithmetic.ABS] = displayIntoIota { display -> DoubleIota(display.getRoot().length.toDouble()) }
        implementations[Arithmetic.REV] = displayIntoIota { display -> display.modifyRootBuilder(StringBuilder::reverse) }
        implementations[Arithmetic.INDEX_OF] = twoDisplayIntoIota { a, b ->
            DoubleIota(
                a.getRoot().indexOf(b.getRoot()).toDouble()
            )
        }
        implementations[Arithmetic.REMOVE] = displayNumberIntoIota { display, number -> display.modifyRootBuilder { it.deleteCharAt(number.roundToInt()) } }

        implementations[Arithmetic.APPEND] = twoDisplayIntoIota { a, b -> DisplayIota(a.getWithNewChildren(a.getChildren().map(Component::copy).plus(b.text.copy()))) }
        implementations[Arithmetic.UNAPPEND] = object : OperatorBasic(1, IotaMultiPredicate.all(IotaPredicate.ofType(DisplayIota.TYPE))) {
            override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
                val args = iotas.iterator().withIndex()
                val display = args.next().value as DisplayIota
                val children = display.getChildren().map(Component::copy).toMutableList()
                val removed = children.removeLastOrNull()?.let(::DisplayIota) ?: NullIota()
                return listOf(DisplayIota(display.getWithNewChildren(children)), removed)
            }
        }

        implementations[Arithmetic.REPLACE] = object : OperatorBasic(3, IotaMultiPredicate.triple(IotaPredicate.ofType(DisplayIota.TYPE), IotaPredicate.ofType(
            HexIotaTypes.DOUBLE), IotaPredicate.ofType(DisplayIota.TYPE))) {
            override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
                val args = iotas.iterator().withIndex()
                val a = args.next().value as DisplayIota
                val root = a.getRoot()
                val pos = args.nextPositiveIntUnderInclusive(root.length, arity)
                val b = args.next().value as DisplayIota
                return listOf(a.modifyRootString { it.replaceRange(pos, pos + 1, b.getRoot()).toString() })
            }
        }

        implementations[Arithmetic.INDEX] = displayNumberIntoIota { display, number ->
            val copy = display.getRoot().getOrNull(number.roundToInt()) ?: return@displayNumberIntoIota NullIota()
            display.getWithNewRoot(copy.toString())
        }

        implementations[Arithmetic.REMOVE] = displayNumberIntoIota { display, number ->
            val root = display.getRoot()
            display.modifyRootBuilder { it.deleteCharAt(number.roundToInt().coerceIn(0, root.length - 1)) }
        }

        implementations[Arithmetic.SLICE] = object : OperatorBasic(3, IotaMultiPredicate.triple(IotaPredicate.ofType(DisplayIota.TYPE), IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(HexIotaTypes.DOUBLE))) {
            override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
                val args = iotas.iterator().withIndex()
                val display = args.next().value as DisplayIota
                val root = display.getRoot()
                val index0 = args.nextPositiveIntUnderInclusive(root.length, arity)
                val index1 = args.nextPositiveIntUnderInclusive(root.length, arity)
                return listOf(display.modifyRootString { it.substring(min(index0, index1), max(index0, index1)).toString() })
            }
        }
    }

    override fun arithName() = "display_arithmetic"
    override fun opTypes() = implementations.keys
    override fun getOperator(pattern: HexPattern) = implementations[pattern]

    private fun displayIntoIota(op: (DisplayIota) -> Iota) =
        OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(DisplayIota.TYPE))) { display ->
            op(display as DisplayIota)
        }
    private fun twoDisplayIntoIota(op: (DisplayIota, DisplayIota) -> Iota) =
        OperatorBinary(IotaMultiPredicate.all(IotaPredicate.ofType(DisplayIota.TYPE))) { a, b ->
            op(a as DisplayIota, b as DisplayIota)
        }
    private fun displayNumberIntoIota(op: (DisplayIota, Double) -> Iota) =
        OperatorBinary(
            IotaMultiPredicate.pair(
                IotaPredicate.ofType(DisplayIota.TYPE),
                IotaPredicate.ofType(HexIotaTypes.DOUBLE)
            )
        ) { display, number ->
            op(display as DisplayIota, (number as DoubleIota).double)
        }
}