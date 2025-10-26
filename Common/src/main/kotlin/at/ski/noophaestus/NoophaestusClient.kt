package at.ski.noophaestus

import at.ski.noophaestus.config.NoophaestusClientConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object NoophaestusClient {
    fun init() {
        NoophaestusClientConfig.init()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(NoophaestusClientConfig.GlobalConfig::class.java, parent).get()
    }
}
