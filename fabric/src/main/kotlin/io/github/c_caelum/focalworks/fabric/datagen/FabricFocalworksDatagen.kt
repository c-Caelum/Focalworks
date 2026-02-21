package io.github.c_caelum.focalworks.fabric.datagen

import io.github.c_caelum.focalworks.datagen.FocalworksActionTags
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object FabricFocalworksDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        pack.addProvider(::FocalworksActionTags)
    }
}
