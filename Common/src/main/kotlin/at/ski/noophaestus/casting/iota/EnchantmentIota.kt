package at.ski.noophaestus.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance


class EnchantmentIota(enchantment: EnchantmentInstance, val isGreater: Boolean) : Iota(if (isGreater) TYPE_GREATER else TYPE, enchantment) {
    override fun isTruthy(): Boolean {
        return true
    }

    val enchantment = this.payload as EnchantmentInstance

    override fun toleratesOther(that: Iota): Boolean {
        return typesMatch(this, that) && that is EnchantmentIota &&
                this.enchantment.enchantment == that.enchantment.enchantment &&
                this.enchantment.level == that.enchantment.level
    }

    override fun serialize(): Tag {
        val nbt = CompoundTag()
        nbt.putString(TAG_ID, BuiltInRegistries.ENCHANTMENT.getKey(enchantment.enchantment).toString())
        nbt.putShort(TAG_LEVEL, enchantment.level.toShort())
        nbt.putBoolean(TAG_POWER, isGreater)
        return nbt
    }

    companion object {
        const val TAG_ID = "id"
        const val TAG_LEVEL = "lvl"
        const val TAG_POWER = "noophaestus:pwr"

        // Helper function to create IotaType with a specific color
        private fun createType(color: Int, isGreater: Boolean): IotaType<EnchantmentIota> = object : IotaType<EnchantmentIota>() {
            override fun deserialize(
                tag: Tag,
                world: ServerLevel?
            ): EnchantmentIota {
                val compound = tag as CompoundTag
                val enchantment = requireNotNull(BuiltInRegistries.ENCHANTMENT.get(ResourceLocation(compound.getString(TAG_ID)))) {
                    "Null enchantment ID"
                }
                val level = compound.getShort(TAG_LEVEL)
                val enchantmentInstance = EnchantmentInstance(enchantment, level.toInt())

                return EnchantmentIota(enchantmentInstance, isGreater)
            }

            override fun display(tag: Tag): Component {
                val enchantmentInstance = deserialize(tag, null).enchantment
                return enchantmentInstance.enchantment.getFullname(enchantmentInstance.level).copy().withStyle { it.withColor(color) }.append(": [enchantment:"+ BuiltInRegistries.ENCHANTMENT.getKey(enchantmentInstance.enchantment) + ":" + enchantmentInstance.level + "]")
            }

            override fun color(): Int {
                return color
            }
        }

        var TYPE: IotaType<EnchantmentIota> = createType(0xff_8932B8.toInt(), false)  // Default purple
        var TYPE_GREATER: IotaType<EnchantmentIota> = createType(0xff_FF0000.toInt(), true)  // Red for greater
    }
}

fun EnchantmentInstance.asActionResult(isGreater: Boolean = false) = listOf(EnchantmentIota(this, isGreater))

fun List<Iota>.getEnchantment(idx: Int, argc: Int = 0): EnchantmentInstance {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is EnchantmentIota)
        return x.enchantment
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "enchantment")
}