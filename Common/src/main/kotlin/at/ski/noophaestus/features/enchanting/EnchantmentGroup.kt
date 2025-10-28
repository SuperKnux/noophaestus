package at.ski.noophaestus.features.enchanting

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
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


    public fun getEnchantmentInstances(): List<EnchantmentInstance> {
        val instanceList = enchantments.flatMap { pair -> listOf(pair.first) }
        return instanceList
    }

    public fun getActiveEnchantmentInstance(): EnchantmentInstance {
        return enchantments.first { pair -> pair.second }.first
    }

    fun setActiveEnchantmentInstance(i: Int) {
        enchantments[i] = enchantments[i].copy(second = true)
        for(index in enchantments.indices) {
            if(index != i) {
                enchantments[index] = enchantments[index].copy(second = false)
            }
        }
    }

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
            Codec.STRING.xmap(
                { s -> Component.literal(s).copy() },
                { comp -> comp.string }
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