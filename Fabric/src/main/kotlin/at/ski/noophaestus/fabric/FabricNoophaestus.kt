package at.ski.noophaestus.fabric

import at.ski.noophaestus.Noophaestus
import net.fabricmc.api.ModInitializer

object FabricNoophaestus : ModInitializer {
    override fun onInitialize() {
        Noophaestus.init()
    }
}
