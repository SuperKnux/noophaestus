package at.ski.noophaestus

import at.ski.noophaestus.config.NoophaestusClientConfig
import at.ski.noophaestus.interop.inline.InlineNoophaestusClient
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object NoophaestusClient {
    fun init() {
        NoophaestusClientConfig.init()
        InlineNoophaestusClient.initClient()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(NoophaestusClientConfig.GlobalConfig::class.java, parent).get()
    }
}
