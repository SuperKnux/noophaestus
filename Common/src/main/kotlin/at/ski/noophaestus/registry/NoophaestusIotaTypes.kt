package at.ski.noophaestus.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.EnchantmentIota

object NoophaestusIotaTypes : NoophaestusRegistrar<IotaType<*>>(
    HexRegistries.IOTA_TYPE,
    { HexIotaTypes.REGISTRY },
) {
    val ENCHANTMENT = register("enchantment") { EnchantmentIota.TYPE }
    val GREATER_ENCHANTMENT = register("enchantment/greater") { EnchantmentIota.TYPE_GREATER }
    val ENCHANTMENT_GROUP = register("enchantment_group") { EnchantmentGroupIota.TYPE }
}