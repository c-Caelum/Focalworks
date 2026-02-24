package io.github.c_caelum.focalworks.registry

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.core.Registry
import io.github.c_caelum.focalworks.Focalworks.id

object FocalworksIotas {
    fun initIotas() {

    }
    fun <IOTA : Iota> iotaType(name : String, type : IotaType<IOTA>) : IotaType<IOTA> {
        return Registry.register(HexIotaTypes.REGISTRY, id(name), type)
    }
}

