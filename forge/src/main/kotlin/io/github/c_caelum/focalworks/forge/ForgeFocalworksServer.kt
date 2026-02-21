package io.github.c_caelum.focalworks.forge

import io.github.c_caelum.focalworks.Focalworks
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent

object ForgeFocalworksServer {
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLDedicatedServerSetupEvent) {
        Focalworks.initServer()
    }
}
