package io.github.c_caelum.focalworks.networking

import dev.architectury.networking.NetworkChannel
import io.github.c_caelum.focalworks.Focalworks
import io.github.c_caelum.focalworks.networking.msg.FocalworksMessageCompanion

object FocalworksNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Focalworks.id("networking_channel"))

    fun init() {
        for (subclass in FocalworksMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
