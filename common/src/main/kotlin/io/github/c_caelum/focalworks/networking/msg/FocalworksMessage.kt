package io.github.c_caelum.focalworks.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import io.github.c_caelum.focalworks.Focalworks
import io.github.c_caelum.focalworks.networking.FocalworksNetworking
import io.github.c_caelum.focalworks.networking.handler.applyOnClient
import io.github.c_caelum.focalworks.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface FocalworksMessage

sealed interface FocalworksMessageC2S : FocalworksMessage {
    fun sendToServer() {
        FocalworksNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface FocalworksMessageS2C : FocalworksMessage {
    fun sendToPlayer(player: ServerPlayer) {
        FocalworksNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        FocalworksNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface FocalworksMessageCompanion<T : FocalworksMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Focalworks.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is FocalworksMessageC2S -> msg.applyOnServer(ctx)
                    else -> Focalworks.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Focalworks.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is FocalworksMessageS2C -> msg.applyOnClient(ctx)
                    else -> Focalworks.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
