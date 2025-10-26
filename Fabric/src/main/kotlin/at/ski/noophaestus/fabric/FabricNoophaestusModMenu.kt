package at.ski.noophaestus.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import at.ski.noophaestus.NoophaestusClient

object FabricNoophaestusModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(NoophaestusClient::getConfigScreen)
}
