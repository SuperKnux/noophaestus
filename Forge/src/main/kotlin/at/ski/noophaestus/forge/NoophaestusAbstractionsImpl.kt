@file:JvmName("NoophaestusAbstractionsImpl")

package at.ski.noophaestus.forge

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.ski.noophaestus.forge.casting.iota.DisplayIota
import at.ski.noophaestus.registry.NoophaestusRegistrar
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraftforge.registries.RegisterEvent
import ram.talia.moreiotas.api.casting.iota.StringIota
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: NoophaestusRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}

fun getPlatformIota(iota: Iota, world: ServerLevel) : Component {
    return when (iota) {
        is DisplayIota -> {
            iota.text
        }
        is StringIota -> {
            Component.literal(iota.string)
        }
        else -> throw MishapInvalidIota.of(iota, 0, "stringidentifiable")
    }
}