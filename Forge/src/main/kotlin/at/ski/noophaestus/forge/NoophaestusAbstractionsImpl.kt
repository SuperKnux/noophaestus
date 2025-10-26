@file:JvmName("NoophaestusAbstractionsImpl")

package at.ski.noophaestus.forge

import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
