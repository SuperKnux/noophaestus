package at.ski.noophaestus.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.list.*
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.function.Function

object EnchantmentArithmetic : Arithmetic {

    @JvmField
    val OPS = listOf(
        ABS
    )

    override fun arithName() = "enchantment_ops"

    override fun opTypes(): Iterable<HexPattern> = OPS

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {

            ABS -> make1Double { it.level.toDouble() }
            else -> throw InvalidOperatorException("\$pattern is not a valid operator in Arithmetic \$this.")
        }
    }

    private fun make1Double(op: Function<EnchantmentInstance, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(ofType(ENCHANTMENT.value))) {
        i: Iota -> DoubleIota(
            op.apply(Operator.downcast(i, ENCHANTMENT.value).enchantment)
        )
    }
}