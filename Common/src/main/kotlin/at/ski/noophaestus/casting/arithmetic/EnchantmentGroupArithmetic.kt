package at.ski.noophaestus.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.ski.noophaestus.casting.arithmetic.operator.enchantmentgroup.OpEnchantmentGroupRemove
import at.ski.noophaestus.casting.arithmetic.operator.enchantmentgroup.OpEnchantmentGroupUnappend
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota.*
import at.ski.noophaestus.casting.iota.EnchantmentIota
import at.ski.noophaestus.casting.iota.asActionResult
import at.ski.noophaestus.features.enchanting.EnchGroupBuilder
import at.ski.noophaestus.features.enchanting.EnchantmentGroup
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT
import at.ski.noophaestus.registry.NoophaestusIotaTypes.ENCHANTMENT_GROUP
import ram.talia.moreiotas.api.asActionResult
import java.util.function.Function

object EnchantmentGroupArithmetic : Arithmetic {

    @JvmField
    val OPS = listOf(
        ABS,
        APPEND,
        UNAPPEND,
        REMOVE,
    )

    override fun arithName(): String {
        return "enchantment_group_ops"
    }

    override fun opTypes(): Iterable<HexPattern> {
        return OPS
    }

    override fun getOperator(pattern: HexPattern): Operator {
        return when (pattern) {

            ABS -> enchantmentGroupToIota { DoubleIota(it.enchantments.enchantments.size.toDouble()) }
            APPEND -> enchantmentGroupGreaterEnchantmentToIota { enchGroup, gEnchant -> enchGroup.modifyEnchantmentGroup { it.add(gEnchant.enchantment, false).build() } }
            UNAPPEND -> OpEnchantmentGroupUnappend
            REMOVE -> OpEnchantmentGroupRemove
            else -> throw InvalidOperatorException("\$pattern is not a valid operator in Arithmetic \$this.")
        }
    }

    private fun enchantmentGroupToIota(op: (EnchantmentGroupIota) -> Iota) =
        OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(EnchantmentGroupIota.TYPE))) {
            enchantmentGroup -> op(enchantmentGroup as EnchantmentGroupIota)
        }

    private fun enchantmentGroupLesserEnchantmentToIota(op: (EnchantmentGroupIota, EnchantmentIota) -> Iota) =
        OperatorBinary(
            IotaMultiPredicate.pair(
                ofType(EnchantmentGroupIota.TYPE),
                ofType(EnchantmentIota.TYPE)
            )
        ) { enchantmentGroup, enchantment ->
            op(enchantmentGroup as EnchantmentGroupIota, enchantment as EnchantmentIota)
        }

    private fun enchantmentGroupGreaterEnchantmentToIota(op: (EnchantmentGroupIota, EnchantmentIota) -> Iota) =
        OperatorBinary(
            IotaMultiPredicate.pair(
                ofType(EnchantmentGroupIota.TYPE),
                ofType(EnchantmentIota.TYPE_GREATER)
            )
        ) { enchantmentGroup, enchantment ->
            op(enchantmentGroup as EnchantmentGroupIota, enchantment as EnchantmentIota)
        }
}