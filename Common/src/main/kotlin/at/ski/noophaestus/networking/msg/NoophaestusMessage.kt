package at.ski.noophaestus.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import at.ski.noophaestus.Noophaestus
import at.ski.noophaestus.networking.NoophaestusNetworking
import at.ski.noophaestus.networking.handler.applyOnClient
import at.ski.noophaestus.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface NoophaestusMessage

sealed interface NoophaestusMessageC2S : NoophaestusMessage {
    fun sendToServer() {
        NoophaestusNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface NoophaestusMessageS2C : NoophaestusMessage {
    fun sendToPlayer(player: ServerPlayer) {
        NoophaestusNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        NoophaestusNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface NoophaestusMessageCompanion<T : NoophaestusMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Noophaestus.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is NoophaestusMessageC2S -> msg.applyOnServer(ctx)
                    else -> Noophaestus.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Noophaestus.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is NoophaestusMessageS2C -> msg.applyOnClient(ctx)
                    else -> Noophaestus.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
