@file:JvmName("NoophaestusAbstractions")

package at.ski.noophaestus

import dev.architectury.injectables.annotations.ExpectPlatform
import at.ski.noophaestus.registry.NoophaestusRegistrar

fun initRegistries(vararg registries: NoophaestusRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    throw AssertionError()
}
