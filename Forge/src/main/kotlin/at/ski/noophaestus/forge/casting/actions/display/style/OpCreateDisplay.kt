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

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import at.ski.noophaestus.forge.casting.iota.asActionResult
import net.minecraft.network.chat.Component

object OpCreateDisplay : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (args[0] is DisplayIota)
            return Component.literal((args[0] as DisplayIota).getRoot()).asActionResult
        return args[0].display().asActionResult
    }
}