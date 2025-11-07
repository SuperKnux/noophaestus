package at.ski.noophaestus.forge

import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.forge.datagen.ForgeNoophaestusDatagen
import at.ski.noophaestus.forge.registry.ForgeNoophaestusActions
import at.ski.noophaestus.forge.registry.ForgeNoophaestusArithmetics
import at.ski.noophaestus.forge.registry.ForgeNoophaestusIotaTypes
import at.ski.noophaestus.forge.utils.ChatHandler
import at.ski.noophaestus.initRegistries
import dev.architectury.platform.forge.EventBuses
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
        ChatHandler.init()
        initRegistries(
            ForgeNoophaestusActions,
            ForgeNoophaestusIotaTypes,
            ForgeNoophaestusArithmetics,
        )
    }
}
