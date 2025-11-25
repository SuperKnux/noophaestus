package at.ski.noophaestus.forge.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraft.resources.ResourceLocation

object ForgeNoophaestusIotaTypes : NoophaestusRegistrar<IotaType<*>>(
    HexRegistries.IOTA_TYPE,
    { HexIotaTypes.REGISTRY },
) {
    val DISPLAY = register(ResourceLocation("hexpose", "display")) { DisplayIota.TYPE }
}