package caelum.focalworks;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import caelum.focalworks.registry.FocalworksActions;

public class Focalworks implements ModInitializer {
	public static final String MOD_ID = "focalworks";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        FocalworksActions.init();
		LOGGER.info("Focalworks' actions registering!");
	}
    public static Integer clamp(int val, int min, int max) {
        if (val > max) {
            return max;
        }
        return Math.min(max,Math.max(val, min));
    }
}