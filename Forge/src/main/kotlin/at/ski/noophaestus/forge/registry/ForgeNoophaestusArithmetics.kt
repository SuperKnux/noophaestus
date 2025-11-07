package at.ski.noophaestus.forge.registry

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.ski.noophaestus.forge.casting.actions.display.arithmetic.DisplayArithmetic
import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraft.resources.ResourceLocation

object ForgeNoophaestusArithmetics : NoophaestusRegistrar<Arithmetic>(
    HexRegistries.ARITHMETIC,
    { HexArithmetics.REGISTRY },
) {
    val DISPLAY = register(ResourceLocation("hexpose", "display")) { DisplayArithmetic }
}