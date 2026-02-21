package io.github.c_caelum.focalworks

import io.github.c_caelum.focalworks.config.FocalworksClientConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object FocalworksClient {
    fun init() {
        FocalworksClientConfig.init()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(FocalworksClientConfig.GlobalConfig::class.java, parent).get()
    }
}
