package at.ski.noophaestus.fabric

import at.ski.noophaestus.NoophaestusClient
import net.fabricmc.api.ClientModInitializer

object FabricNoophaestusClient : ClientModInitializer {
    override fun onInitializeClient() {
        NoophaestusClient.init()
    }
}
