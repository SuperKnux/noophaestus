package at.ski.noophaestus.fabric

import at.ski.noophaestus.Noophaestus
import net.fabricmc.api.DedicatedServerModInitializer

object FabricNoophaestusServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        Noophaestus.initServer()
    }
}
