@file:JvmName("NoophaestusAbstractionsImpl")

package at.ski.noophaestus.fabric

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.ski.noophaestus.registry.NoophaestusRegistrar
import miyucomics.hexpose.iotas.TextIota
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import ram.talia.moreiotas.api.casting.iota.StringIota

fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}

fun getPlatformIota(iota: Iota, world: ServerLevel) : Component {
    return when (iota) {
        is TextIota -> {
            iota.text
        }
        is StringIota -> {
            Component.literal(iota.string)
        }
        else -> throw MishapInvalidIota.of(iota, 0, "stringidentifiable")
    }
}
