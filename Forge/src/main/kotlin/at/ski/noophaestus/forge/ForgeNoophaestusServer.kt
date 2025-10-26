package at.ski.noophaestus.forge

import at.ski.noophaestus.Noophaestus
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent

object ForgeNoophaestusServer {
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLDedicatedServerSetupEvent) {
        Noophaestus.initServer()
    }
}
