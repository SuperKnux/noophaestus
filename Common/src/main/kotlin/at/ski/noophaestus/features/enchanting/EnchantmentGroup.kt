package at.ski.noophaestus.features.enchanting

import net.minecraft.network.chat.MutableComponent
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

}