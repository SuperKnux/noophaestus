package at.ski.noophaestus.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.ABS
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.ski.noophaestus.casting.iota.asActionResult
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT_GROUP
import java.util.function.Function

object EnchantmentGroupArithmetic : Arithmetic {

    @JvmField
    val OPS = listOf(
        ABS
    )

    override fun arithName(): String {
        return "enchantment_group_ops"
    }

    override fun opTypes(): Iterable<HexPattern> {
        return OPS
    }

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {

            ABS -> make1Double { it.enchantments.size.toDouble() }
            else -> throw InvalidOperatorException("\$pattern is not a valid operator in Arithmetic \$this.")
        }
    }

    private fun make1Double(op: Function<EnchantmentGroup, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(ofType(ENCHANTMENT_GROUP.value))) {
        i: Iota -> DoubleIota(
            op.apply(Operator.downcast(i, ENCHANTMENT_GROUP.value).enchantments)
        )
    }
}