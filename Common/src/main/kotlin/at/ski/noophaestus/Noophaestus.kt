package at.ski.noophaestus

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import at.ski.noophaestus.config.NoophaestusServerConfig
import at.ski.noophaestus.networking.NoophaestusNetworking
import at.ski.noophaestus.registry.NoophaestusActions
import at.ski.noophaestus.registry.NoophaestusArithmetics
import at.ski.noophaestus.registry.NoophaestusIotaTypes

object Noophaestus {
    const val MODID = "noophaestus"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        NoophaestusServerConfig.init()
        initRegistries(
            NoophaestusActions,
            NoophaestusIotaTypes,
            NoophaestusArithmetics,
        )
        NoophaestusNetworking.init()
    }

    fun initServer() {
        NoophaestusServerConfig.initServer()
    }
}
