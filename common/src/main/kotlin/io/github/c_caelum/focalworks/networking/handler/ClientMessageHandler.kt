package io.github.c_caelum.focalworks.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import io.github.c_caelum.focalworks.config.FocalworksServerConfig
import io.github.c_caelum.focalworks.networking.msg.*

fun FocalworksMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            FocalworksServerConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
