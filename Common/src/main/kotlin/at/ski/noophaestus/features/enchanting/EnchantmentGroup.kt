package at.ski.noophaestus.features.enchanting

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.getString
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.TagParser
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

/**
 * Houses an array of enchantments, with only one of them active at a time.
 * There must be at least one enchantment in the group.
 */

class EnchantmentGroup(var name: MutableComponent, var enchantments: MutableList<Pair<EnchantmentInstance, Boolean>>) {


    companion object {
        private val ENCHANTMENT_CODEC: Codec<Enchantment> =
            ResourceLocation.CODEC.xmap(
                { rl -> BuiltInRegistries.ENCHANTMENT.get(rl) },
                { ench -> BuiltInRegistries.ENCHANTMENT.getKey(ench) }
            )

        // Codec for EnchantmentInstance: { "id": "minecraft:sharpness", "level": 5 }
        private val ENCHANTMENT_INSTANCE_CODEC: Codec<EnchantmentInstance> =
            RecordCodecBuilder.create { inst ->
                inst.group(
                    ENCHANTMENT_CODEC.fieldOf("id").forGetter { it.enchantment },
                    Codec.INT.fieldOf("level").forGetter { it.level }
                ).apply(inst) { ench, lvl -> EnchantmentInstance(ench, lvl) }
            }

        // Codec for Pair<EnchantmentInstance, Boolean>: { "enchantment": { ... }, "active": true }
        private val ENCH_PAIR_CODEC: Codec<Pair<EnchantmentInstance, Boolean>> =
            RecordCodecBuilder.create { inst ->
                inst.group(
                    ENCHANTMENT_INSTANCE_CODEC.fieldOf("enchantment").forGetter { it.first },
                    Codec.BOOL.fieldOf("active").forGetter { it.second }
                ).apply(inst) { ei, active -> ei to active }
            }

        // Simple representation of the group name as a string; encode/decode using literal component
        private val NAME_CODEC: Codec<MutableComponent> =
            CompoundTag.CODEC.xmap(
                { s ->
                    if (!s.contains("name")) {
                        Component.Serializer.toJson(Component.literal("arimfexendrapuse").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.OBFUSCATED))
                    }
                    when (val tag = s.get("name")) {
                        is CompoundTag -> Component.Serializer.fromJson(tag.asCompound.getString("text") ?: Component.Serializer.toJson(Component.literal("arimfexendrapuse").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.OBFUSCATED)))
                        is StringTag -> Component.Serializer.fromJson(Component.Serializer.toJson(Component.literal(tag.asString)))
                        else -> Component.literal("arimfexendrapuse").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.OBFUSCATED)
                    }
                },
                { comp ->
                    TagParser.parseTag(Component.Serializer.toJson(comp))
                }
            )

        // Final Codec for EnchantmentGroup:
        // {
        //   "name": "My Group",
        //   "enchantments": [
        //     { "enchantment": { "id": "minecraft:sharpness", "level": 5 }, "active": true },
        //     { "enchantment": { "id": "minecraft:smite", "level": 4 }, "active": false }
        //   ]
        // }
        val CODEC: Codec<EnchantmentGroup> =
            RecordCodecBuilder.create { inst ->
                inst.group(
                    NAME_CODEC.fieldOf("name").forGetter { it.name },
                    ENCH_PAIR_CODEC.listOf().fieldOf("enchantments").forGetter { it.enchantments }
                ).apply(inst) { name, list -> EnchantmentGroup(name, list.toMutableList()) }
            }
    }

}