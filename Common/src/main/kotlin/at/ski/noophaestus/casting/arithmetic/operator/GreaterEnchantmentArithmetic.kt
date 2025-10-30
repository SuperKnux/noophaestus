package at.ski.noophaestus.casting.arithmetic.operator

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.arithmetic.operator.list.*
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.EnchantmentIota
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.function.Function

object GreaterEnchantmentArithmetic : Arithmetic {

    @JvmField
    val OPS = listOf(
        ABS
    )

    override fun arithName() = "greater_enchantment_ops"

    override fun opTypes(): Iterable<HexPattern> = OPS

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {
            ABS -> enchantmentInstanceToIota { DoubleIota(it.enchantment.level.toDouble()) }
            else -> throw InvalidOperatorException("\$pattern is not a valid operator in Arithmetic \$this.")
        }
    }

    private fun enchantmentInstanceToIota(op: (EnchantmentIota) -> Iota) =
        OperatorUnary(IotaMultiPredicate.all(ofType(EnchantmentIota.TYPE_GREATER))) {
                enchantment -> op(enchantment as EnchantmentIota)
        }
}