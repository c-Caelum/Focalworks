package io.github.c_caelum.focalworks.fabric

import io.github.c_caelum.focalworks.Focalworks
import net.fabricmc.api.ModInitializer

object FabricFocalworks : ModInitializer {
    override fun onInitialize() {
        Focalworks.init()
    }
}
