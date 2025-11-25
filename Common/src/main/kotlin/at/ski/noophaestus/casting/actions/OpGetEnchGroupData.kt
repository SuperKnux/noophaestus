package at.ski.noophaestus.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.ski.noophaestus.casting.iota.EnchantmentGroupIota
import at.ski.noophaestus.casting.iota.asActionResult
import at.ski.noophaestus.casting.iota.getEnchantmentGroup

class OpGetEnchGroupData(private val process: (CastingEnvironment, EnchantmentGroupIota) -> List<Iota>) : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val enchantmentGroupIota = args.getEnchantmentGroup(0, argc).asActionResult()[0]
        return process(env, enchantmentGroupIota)
    }
}