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
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import net.minecraft.network.chat.TextColor
import net.minecraft.world.phys.Vec3

object OpDisplayColor : Action {
    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
        val stack = image.stack.toMutableList()
        if (stack.isEmpty())
            throw MishapNotEnoughArgs(1, 0)

        val top = stack.last()
        if (top is DisplayIota) {
            stack.removeAt(stack.lastIndex)
            val color = top.text.style.color?.let {
                val r = ((it.value shr 16) and 0xFF) / 255.0
                val g = ((it.value shr 8) and 0xFF) / 255.0
                val b = (it.value and 0xFF) / 255.0
                Vec3Iota(Vec3(r, g, b))
            } ?: NullIota()

            stack.add(color)
            return OperationResult(
                image.copy(stack = stack).withUsedOp(),
                listOf(),
                continuation,
                HexEvalSounds.NORMAL_EXECUTE
            )
        }

        if (stack.size == 1)
            throw MishapNotEnoughArgs(2, 1)

        val text = stack[stack.lastIndex - 1]
        if (text !is DisplayIota)
            throw MishapInvalidIota.ofType(text, 1, "text")

        val color = when (val colorRaw = stack[stack.lastIndex]) {
            is Vec3Iota -> TextColor.fromRgb(colorRaw.vec3.let {
                (it.x.coerceIn(0.0, 1.0) * 255).toInt() shl 16 or
                        (it.y.coerceIn(0.0, 1.0) * 255).toInt() shl 8 or
                        (it.z.coerceIn(0.0, 1.0) * 255).toInt()
            })
            is NullIota -> null
            else -> throw MishapInvalidIota.of(colorRaw, 0, "vector_or_null")
        }

        stack.removeAt(stack.lastIndex)
        stack.removeAt(stack.lastIndex)
        stack.add(DisplayIota.createSanitized(text.text.copy().setStyle(text.text.style.withColor(color))))
        return OperationResult(
            image.copy(stack = stack).withUsedOp(),
            listOf(),
            continuation,
            HexEvalSounds.NORMAL_EXECUTE
        )
    }
}