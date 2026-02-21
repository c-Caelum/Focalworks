@file:JvmName("FocalworksAbstractions")

package io.github.c_caelum.focalworks

import dev.architectury.injectables.annotations.ExpectPlatform
import io.github.c_caelum.focalworks.registry.FocalworksRegistrar

fun initRegistries(vararg registries: FocalworksRegistrar<*>) {
    for (registry in registries) {
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: FocalworksRegistrar<T>) {
    throw AssertionError()
}
