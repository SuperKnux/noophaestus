package at.ski.noophaestus.casting.arithmetic.operator.enchantmentgroup

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.*
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.features.enchanting.EnchGroupBuilder
import at.ski.noophaestus.features.enchanting.EnchantmentGroup

object OpEnchantmentGroupRemove : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(EnchantmentGroupIota.TYPE), IotaPredicate.ofType(DOUBLE))) {
    override fun apply(
        iotas: Iterable<Iota>,
        env: CastingEnvironment
    ): Iterable<Iota> {
        val it = iotas.iterator()
        val enchGroup = downcast(it.next(), EnchantmentGroupIota.TYPE).enchantments
        val index = downcast(it.next(), DOUBLE).double.toInt()
        val newEnchGroup = EnchGroupBuilder.from(enchGroup).removeAt(index).build()
        return listOf(EnchantmentGroupIota(enchGroup.name, newEnchGroup))
    }

}