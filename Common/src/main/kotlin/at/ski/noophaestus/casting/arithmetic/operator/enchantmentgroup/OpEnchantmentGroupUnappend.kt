package at.ski.noophaestus.casting.arithmetic.operator.enchantmentgroup

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.EnchantmentIota
import at.ski.noophaestus.features.enchanting.EnchGroupBuilder

object OpEnchantmentGroupUnappend : OperatorBasic(1, IotaMultiPredicate.all(IotaPredicate.ofType(EnchantmentGroupIota.TYPE))) {
    override fun apply(
        iotas: Iterable<Iota>,
        env: CastingEnvironment
    ): Iterable<Iota> {
        val it = iotas.iterator()
        val enchGroup = downcast(it.next(), EnchantmentGroupIota.TYPE)
        val (last, newGroup) = EnchGroupBuilder.from(enchGroup.enchantments).pop()

        val result: Iota = last?.let { EnchantmentIota(it, true) } ?: NullIota()
        val newGroupIota = EnchantmentGroupIota(enchGroup.name, newGroup.build())
        return listOf(result, newGroupIota)
    }
}