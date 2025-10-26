package at.ski.noophaestus.networking

import dev.architectury.networking.NetworkChannel
import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.networking.msg.NoophaestusMessageCompanion

object NoophaestusNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Noophaestus.id("networking_channel"))

    fun init() {
        for (subclass in NoophaestusMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
