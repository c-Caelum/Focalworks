@file:JvmName("FocalworksAbstractionsImpl")

package io.github.c_caelum.focalworks.fabric

import io.github.c_caelum.focalworks.registry.FocalworksRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: FocalworksRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
