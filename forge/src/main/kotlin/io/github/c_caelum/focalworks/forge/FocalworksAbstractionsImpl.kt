@file:JvmName("FocalworksAbstractionsImpl")

package io.github.c_caelum.focalworks.forge

import io.github.c_caelum.focalworks.registry.FocalworksRegistrar
import net.minecraftforge.registries.RegisterEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

fun <T : Any> initRegistry(registrar: FocalworksRegistrar<T>) {
    MOD_BUS.addListener { event: RegisterEvent ->
        event.register(registrar.registryKey) { helper ->
            registrar.init(helper::register)
        }
    }
}
