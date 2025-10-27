package at.ski.noophaestus.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.asCompound
import at.ski.noophaestus.api.item.ItemStackAccessor
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import com.google.common.base.Objects
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import ram.talia.moreiotas.api.getItemStack

object OpEnchantmentGroupsFromItem : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val stack = args.getItemStack(0, argc)
        val enchantmentGroups = stack.`noophaestus$getEnchantmentGroupTags`()
        enchantmentGroups.map { env.printMessage(EnchantmentGroupIota.TYPE.display(it)) }
        val processedEnchantmentGroups = enchantmentGroups.map { EnchantmentGroupIota.TYPE.deserialize(it, env.world)!! }
        return processedEnchantmentGroups.asActionResult
    }
}