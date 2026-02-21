package io.github.c_caelum.focalworks.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.c_caelum.focalworks.FocalworksClient

object FabricFocalworksModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(FocalworksClient::getConfigScreen)
}
