@file:JvmName("NoophaestusAbstractions")

package at.ski.noophaestus

import at.petrak.hexcasting.api.casting.iota.Iota
import at.ski.noophaestus.registry.NoophaestusRegistrar
import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel

fun initRegistries(vararg registries: NoophaestusRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    throw AssertionError()
}

@ExpectPlatform
fun getPlatformIota(iota: Iota, world: ServerLevel) : Component {
    throw AssertionError()
}
