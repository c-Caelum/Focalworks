package io.github.c_caelum.focalworks.fabric

import io.github.c_caelum.focalworks.FocalworksClient
import net.fabricmc.api.ClientModInitializer

object FabricFocalworksClient : ClientModInitializer {
    override fun onInitializeClient() {
        FocalworksClient.init()
    }
}
