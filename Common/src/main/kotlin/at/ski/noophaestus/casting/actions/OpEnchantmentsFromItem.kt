package at.ski.noophaestus.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getPositiveIntUnder
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.xplat.IXplatAbstractions
import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.casting.iota.EnchantmentIota
import net.minecraft.network.chat.Component
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.getItemStack

object OpEnchantmentsFromItem : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val stack = args.getItemStack(0, argc)
        val enchantments = stack.enchantmentTags
        val enchantmentsToProcess = enchantments.onEach { it.asCompound.putBoolean(EnchantmentIota.TAG_POWER, false) }
        enchantments.map { env.printMessage(EnchantmentIota.TYPE.display(it)) }
        val enchantmentIotas = enchantmentsToProcess.map { requireNotNull(EnchantmentIota.TYPE.deserialize(it, null)) {
            emptyList<Iota>().asActionResult
        } }.asActionResult
        return enchantmentIotas
    }

}