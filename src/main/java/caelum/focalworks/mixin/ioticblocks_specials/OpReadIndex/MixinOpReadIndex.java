package caelum.focalworks.mixin.ioticblocks_specials.OpWriteIndex;


import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import caelum.focalworks.Focalworks;
import caelum.focalworks.api.RiggedHexFinder;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import gay.object.ioticblocks.casting.actions.OpReadIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.HashMap;
import java.util.List;


@Mixin(value = OpReadIndex.class,remap = false)
public class MixinOpReadIndex {
    @Unique
    private SpellList hex = null;
    @Unique
    private boolean is_specialised = false;

    @Inject(target = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private Iota focalworks_new_list(Iota datum) {
        if (!is_specialised && hex != null) {
            HashMap<String, Object> map = Focalworks.CONTEXT.get();
            CastingVM vm = (CastingVM) map.get("vm");
            List<Iota> stack = vm.getImage().getStack();
            stack.remove(stack.size()-1);
            stack.add(datum);
            vm.setImage(RiggedHexFinder.set_image_stack(vm.getImage(),stack));
            Iota result = RiggedHexFinder.cast_rigged_read(vm,hex);
            return result;
        }
        return datum;
    }
}
