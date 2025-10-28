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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
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
        val enchantmentsSecond = ListTag()
        for (enchantment in enchantments) {
            val compound = CompoundTag()
            compound.putString(EnchantmentIota.TAG_ID, enchantment.asCompound.getString("id"))
            compound.putShort(EnchantmentIota.TAG_LEVEL, enchantment.asCompound.getShort("lvl"))
            compound.putBoolean(EnchantmentIota.TAG_POWER, false)
            enchantmentsSecond.add(compound)
        }

        for (enchantment in enchantmentsSecond) {
           val instance = requireNotNull(BuiltInRegistries.ENCHANTMENT.get(ResourceLocation(enchantment.asCompound.getString(EnchantmentIota.TAG_ID)))).getFullname(enchantment.asCompound.getShort(EnchantmentIota.TAG_LEVEL).toInt())
            env.printMessage(Component.literal("Enchantment: ").append(instance))
        }
        val enchantmentIotas = enchantmentsSecond.map { requireNotNull(EnchantmentIota.TYPE_GREATER.deserialize(it, null)) {
            emptyList<Iota>().asActionResult
        } }.asActionResult
        return enchantmentIotas
    }

}