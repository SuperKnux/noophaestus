package at.ski.noophaestus.forge

import dev.architectury.platform.forge.EventBuses
import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.forge.datagen.ForgeNoophaestusDatagen
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Noophaestus.MODID)
class ForgeNoophaestus {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Noophaestus.MODID, this)
            addListener(ForgeNoophaestusClient::init)
            addListener(ForgeNoophaestusDatagen::init)
            addListener(ForgeNoophaestusServer::init)
        }
        Noophaestus.init()
    }
}
