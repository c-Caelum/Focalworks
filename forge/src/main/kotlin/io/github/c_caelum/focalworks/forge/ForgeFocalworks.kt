package io.github.c_caelum.focalworks.forge

import dev.architectury.platform.forge.EventBuses
import io.github.c_caelum.focalworks.Focalworks
import io.github.c_caelum.focalworks.forge.datagen.ForgeFocalworksDatagen
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Focalworks.MOD_ID)
class ForgeFocalworks {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Focalworks.MOD_ID, this)
            addListener(ForgeFocalworksClient::init)
            addListener(ForgeFocalworksDatagen::init)
            addListener(ForgeFocalworksServer::init)
        }
        Focalworks.init()
    }
}
