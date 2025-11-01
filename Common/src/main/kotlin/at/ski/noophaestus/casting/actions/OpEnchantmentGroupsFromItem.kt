package at.ski.noophaestus.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.ski.noophaestus.api.item.ItemStackUtilsKt.noophaestus_enchantmentGroupTags
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import ram.talia.moreiotas.api.getItemStack

object OpEnchantmentGroupsFromItem : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val stack = args.getItemStack(0, argc)
        val stackToProcess = stack.noophaestus_enchantmentGroupTags()
        val processedEnchantmentGroups = stackToProcess.map { EnchantmentGroupIota.TYPE.deserialize(it, env.world)!! }
        return processedEnchantmentGroups.asActionResult
    }
}