package at.ski.noophaestus.api.item

import net.minecraft.nbt.ListTag

interface ItemStackAccessor {
    fun noophaestus_getEnchantmentGroupTags(): ListTag
}