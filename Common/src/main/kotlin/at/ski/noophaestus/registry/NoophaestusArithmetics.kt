package at.ski.noophaestus.registry

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.ski.noophaestus.casting.arithmetic.EnchantmentArithmetic
import at.ski.noophaestus.casting.arithmetic.EnchantmentGroupArithmetic

object NoophaestusArithmetics : NoophaestusRegistrar<Arithmetic>(
    HexRegistries.ARITHMETIC,
    { HexArithmetics.REGISTRY },
) {
    val ENCHANTMENT = register("enchantment") { EnchantmentArithmetic }
    val ENCHANTMENT_GROUP = register("enchantment_group") { EnchantmentGroupArithmetic }
}