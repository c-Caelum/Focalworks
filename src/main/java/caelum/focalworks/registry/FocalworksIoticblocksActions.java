package caelum.focalworks.registry;


import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import net.minecraft.core.Registry;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import caelum.focalworks.casting.actions.ioticblocks.*;
import static caelum.focalworks.Focalworks.id;


public class FocalworksIoticblocksActions {
    public static void init() {
        register("rig_write_index","wwdeweeeweqawqwawwwdwwdw",HexDir.EAST,new OpRigWriteIndex());
        register("rig_read_index", "wwaqwqqqwqedwewwewewdwww", HexDir.EAST, new OpRigReadIndex());
    }
    private static ActionRegistryEntry register(
            String name,
            String signature,
            HexDir startDir,
            Action action
    ) {
        return Registry.register(HexActions.REGISTRY, id(name), new ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action));
    }
}
