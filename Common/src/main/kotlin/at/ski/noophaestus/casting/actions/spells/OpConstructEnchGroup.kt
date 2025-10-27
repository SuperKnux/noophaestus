package at.ski.noophaestus.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.EnchantmentIota
import at.ski.noophaestus.casting.iota.EnchantmentIota.Companion.TYPE_GREATER
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import net.minecraft.network.chat.Component
import ram.talia.moreiotas.api.getString

object OpConstructEnchGroup : Action {
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        val stack = image.stack.toMutableList()

        if (stack.isEmpty()) {
            throw MishapNotEnoughArgs(1, 0)
        }
        val name = stack.take(1).getString(0, stack.size - 1)
        stack.removeFirst()

        val yoinkCount = stack.take(1).getPositiveIntUnderInclusive(0, stack.size - 1)
        stack.removeFirst()
        val output = mutableListOf<Iota>()
        output.addAll(stack.takeLast(yoinkCount))
        val enchantmentPairs = output.map {
            when (it.type) {
                TYPE_GREATER -> {
                    val enchantmentIota = it as EnchantmentIota
                    Pair(enchantmentIota.enchantment, true)
                }
                else -> throw MishapInvalidIota.ofType(it, 0, "greater_enchantment")
            }
        }.toMutableList()
        val enchantmentGroup = EnchantmentGroup(Component.literal(name), enchantmentPairs)
        stack.add(EnchantmentGroupIota(name, enchantmentGroup))

        val image2 = image.withUsedOp().copy(stack = stack)
        return OperationResult(image2, listOf(), continuation, HexEvalSounds.THOTH)

    }
}