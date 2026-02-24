package io.github.c_caelum.focalworks

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import io.github.c_caelum.focalworks.config.FocalworksServerConfig
import io.github.c_caelum.focalworks.networking.FocalworksNetworking
import io.github.c_caelum.focalworks.registry.FocalworksActions

object Focalworks {
    const val MOD_ID = "focalworks"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MOD_ID, path)

    fun init() {
        LOGGER.warn(IotaType.serialize(PatternIota(HexPattern.fromAngles("de", HexDir.NORTH_EAST))))
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
