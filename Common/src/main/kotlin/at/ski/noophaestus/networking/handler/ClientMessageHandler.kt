package at.ski.noophaestus.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import at.ski.noophaestus.config.NoophaestusServerConfig
import at.ski.noophaestus.networking.msg.*

fun NoophaestusMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            NoophaestusServerConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
