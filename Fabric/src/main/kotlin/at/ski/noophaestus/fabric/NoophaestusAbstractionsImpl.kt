@file:JvmName("NoophaestusAbstractionsImpl")

package at.ski.noophaestus.fabric

import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
