package io.github.c_caelum.focalworks

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.c_caelum.focalworks.config.FocalworksServerConfig
import io.github.c_caelum.focalworks.networking.FocalworksNetworking
import io.github.c_caelum.focalworks.registry.FocalworksActions

object Focalworks {
    const val MODID = "focalworks"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        FocalworksServerConfig.init()
        initRegistries(
            FocalworksActions,
        )
        FocalworksNetworking.init()
    }

    fun initServer() {
        FocalworksServerConfig.initServer()
    }
}
