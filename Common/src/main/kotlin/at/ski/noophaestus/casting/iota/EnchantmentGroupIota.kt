package at.ski.noophaestus.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asList
import at.petrak.hexcasting.api.utils.putCompound
import at.ski.noophaestus.features.enchanting.EnchGroupBuilder
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.Objects.requireNonNull
import kotlin.let

class EnchantmentGroupIota(var name: String, enchantments: EnchantmentGroup): Iota(TYPE, enchantments)
{
    constructor(name: String, enchantments: MutableList<Pair<EnchantmentIota, Boolean>>): this(name, EnchantmentGroup(
        Component.literal(name).copy(), enchantments.map {
            it.let { (enchantment, bool) ->
                enchantment.enchantment to bool
            }
        } as MutableList<Pair<EnchantmentInstance, Boolean>>)
    )

    constructor(name: MutableComponent, enchantments: MutableList<Pair<EnchantmentIota, Boolean>>): this(name.string, EnchantmentGroup(name, enchantments.map {
             it.let { (enchantment, bool) ->
                enchantment.enchantment to bool
            }
        } as MutableList<Pair<EnchantmentInstance, Boolean>>)
    )

    constructor(name: MutableComponent, enchantments: EnchantmentGroup): this(name.string, enchantments)

    val enchantments = this.payload as EnchantmentGroup

    fun modifyEnchantmentGroup(modifier: (EnchGroupBuilder) -> EnchantmentGroup): EnchantmentGroupIota {
        val builder = modifier(EnchGroupBuilder.from(enchantments))

        return EnchantmentGroupIota(enchantments.name, builder)
    }


    override fun isTruthy(): Boolean {
        return true
    }

    override fun toleratesOther(that: Iota): Boolean {
        return typesMatch(this, that) &&
                that is EnchantmentGroupIota &&
                this.name == that.name &&
                this.enchantments == that.enchantments
    }

    override fun serialize(): Tag {
        val compound = CompoundTag()
        compound.putString(TAG_ENCH_GROUP_NAME, name)
        val enchantmentList = ListTag()

        for (enchantment in enchantments.enchantments) {
            val enchantmentTag = CompoundTag()
            enchantmentTag.putString(EnchantmentIota.TAG_ID, BuiltInRegistries.ENCHANTMENT.getKey(enchantment.first.enchantment).toString())
            enchantmentTag.putShort(EnchantmentIota.TAG_LEVEL, enchantment.first.level.toShort())
            enchantmentTag.putBoolean(TAG_ENCH_GROUP_IS_ACTIVE, enchantment.second)
            enchantmentList.add(enchantmentTag)
        }

        compound.put(TAG_ENCH_GROUP_TAGS, enchantmentList)

        return compound
    }

    companion object {
        const val TAG_ENCH_GROUP_NAME = "noophaestus:enchantment_group_name"
        const val TAG_ENCH_GROUP_TAGS = "noophaestus:enchantment_group_tags"
        const val TAG_ENCH_GROUP_IS_ACTIVE = "noophaestus:is_active"

        var TYPE: IotaType<EnchantmentGroupIota> = object : IotaType<EnchantmentGroupIota>() {
            override fun deserialize(
                tag: Tag,
                world: ServerLevel?
            ): EnchantmentGroupIota {
                val list = tag as CompoundTag
                val name = list.getString(TAG_ENCH_GROUP_NAME)
                val tags = list.getCompound(TAG_ENCH_GROUP_TAGS)
                val enchantmentPairs = tags.asList.map { Pair(EnchantmentIota.TYPE_GREATER.deserialize(it, null)!!, it.asCompound.getBoolean(TAG_ENCH_GROUP_IS_ACTIVE)) }.toMutableList()
                return EnchantmentGroupIota(name, enchantmentPairs)
            }

            override fun display(tag: Tag): Component {
                val enchantmentGroup = deserialize(tag, null).name
                return Component.literal(enchantmentGroup).copy().withStyle { it.withColor(color()) }
            }

            override fun color(): Int {
                return 0xcb00f5
            }
        }
    }
}