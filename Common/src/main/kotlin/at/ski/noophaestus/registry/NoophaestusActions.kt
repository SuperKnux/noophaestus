package at.ski.noophaestus.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.ski.noophaestus.casting.actions.OpEnchantmentGroupsFromItem
import at.ski.noophaestus.casting.actions.OpEnchantmentsFromItem
import at.ski.noophaestus.casting.actions.spells.OpConstructEnchGroup

object NoophaestusActions : NoophaestusRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY },
) {

    val ITEM_ENCHANTMENTS = make("enchantments/item", HexDir.EAST, "wqwawqweewdwe", OpEnchantmentsFromItem)
    val ENCHANTMENT_GROUP_CONSTRUCT = make("enchantment_groups/construct", HexDir.WEST, "wwewdwwewawwawwq", OpConstructEnchGroup)
    val ITEM_ENCHANTMENT_GROUPS = make("enchantment_groups/item", HexDir.EAST, "wwqwawwqwdwwdwwe", OpEnchantmentGroupsFromItem)

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }
}
