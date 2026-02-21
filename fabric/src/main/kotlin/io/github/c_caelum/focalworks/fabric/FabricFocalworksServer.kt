package io.github.c_caelum.focalworks.fabric

import io.github.c_caelum.focalworks.Focalworks
import net.fabricmc.api.DedicatedServerModInitializer

object FabricFocalworksServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        Focalworks.initServer()
    }
}
