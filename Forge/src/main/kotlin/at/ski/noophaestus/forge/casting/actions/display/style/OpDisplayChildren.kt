package at.ski.noophaestus.forge.casting.actions.display.style

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

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.ski.noophaestus.forge.casting.iota.DisplayIota

object OpDisplayChildren : Action {
    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
        val stack = image.stack.toMutableList()
        if (stack.isEmpty())
            throw MishapNotEnoughArgs(1, 0)

        val top = stack.removeAt(stack.lastIndex)
        if (top is DisplayIota) {
            stack.add(ListIota(top.getChildren().map(DisplayIota::createSanitized)))
            return OperationResult(
                image.copy(stack = stack).withUsedOp(),
                listOf(),
                continuation,
                HexEvalSounds.NORMAL_EXECUTE
            )
        }

        if (top !is ListIota)
            throw MishapInvalidIota.of(top, 0, "display_list")
        if (!top.list.all { it is DisplayIota })
            throw MishapInvalidIota.of(top, 0, "display_list")

        val text = stack.removeAt(stack.lastIndex)
        if (text !is DisplayIota)
            throw MishapInvalidIota.ofType(text, 1, "display")

        stack.add(DisplayIota.createSanitized(text.getWithNewChildren(top.list.map { (it as DisplayIota).text })))
        return OperationResult(
            image.copy(stack = stack).withUsedOp(),
            listOf(),
            continuation,
            HexEvalSounds.NORMAL_EXECUTE
        )
    }
}