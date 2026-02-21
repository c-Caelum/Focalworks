package io.github.c_caelum.focalworks.networking.msg

import io.github.c_caelum.focalworks.config.FocalworksServerConfig
import net.minecraft.network.FriendlyByteBuf

data class MsgSyncConfigS2C(val serverConfig: FocalworksServerConfig.ServerConfig) : FocalworksMessageS2C {
    companion object : FocalworksMessageCompanion<MsgSyncConfigS2C> {
        override val type = MsgSyncConfigS2C::class.java

        override fun decode(buf: FriendlyByteBuf) = MsgSyncConfigS2C(
            serverConfig = FocalworksServerConfig.ServerConfig().decode(buf),
        )

        override fun MsgSyncConfigS2C.encode(buf: FriendlyByteBuf) {
            serverConfig.encode(buf)
        }
    }
}
