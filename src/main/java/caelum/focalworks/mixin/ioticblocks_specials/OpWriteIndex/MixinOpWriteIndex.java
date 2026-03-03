package caelum.focalworks.mixin.ioticblocks_specials.OpWriteIndex;


import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
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
import gay.object.ioticblocks.casting.actions.OpWriteIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;


@Mixin(value = OpWriteIndex.class,remap = false)
public class MixinOpWriteIndex {
    /*@Definition(id = "iota", local = @Local(type = Iota.class, name = "iota"))
    @Definition(id = "get", method = "Ljava/util/List;get(I)Ljava/lang/Object;")
    @Expression("iota = ?.get(2)")*/
    @Unique
    private boolean isNotCancelled = true;
    @Unique
    private SpellList hex = null;
    @Unique
    private boolean is_specialised = false;

    @Definition(id = "get", method = "Ljava/util/List;get(I)Ljava/lang/Object;")
    @Definition(id = "Iota", type = Iota.class)
    @Expression(value = "(Iota) ?.get(2)")
    //target = "Ljava/util/List;get(I)Ljava/lang/Object;"
    @ModifyExpressionValue(method = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0), remap = false)
    private Iota focalworks_execute(Iota iota, @Local(name="target") Either<Entity, BlockPos> target,@Local(name="env") CastingEnvironment env) {

        if (target.left().isPresent()) {
            Entity entity = target.left().get();
            if (entity instanceof ItemEntity) {
                hex = RiggedHexFinder.get_rig_item(((ItemEntity) entity).getItem(),env.getWorld(),"riggedwriteindex");
                if (hex == null) {
                    hex = RiggedHexFinder.get_rig_item(((ItemEntity) entity).getItem(),env.getWorld(),"riggedwrite");
                } else {is_specialised = true;}
            }
        } else {
            BlockPos pos = target.right().get();
            hex = RiggedHexFinder.get_rig_vec(pos,env.getWorld(),"riggedwriteindex");
            if (hex == null) {
                hex = RiggedHexFinder.get_rig_vec(pos,env.getWorld(),"riggedwrite");
            } else {is_specialised = true;}
        }
        if (hex != null && is_specialised) {
            HashMap<String, Object> map = Focalworks.CONTEXT.get();
            CastingVM vm = (CastingVM) map.get("vm");
            Pair<Iota, Boolean> result = RiggedHexFinder.cast_rigged_write_with_cancel(vm,hex);
            isNotCancelled = result.getSecond();
            return result.getFirst();
        }
        return iota;
    }

    @ModifyArg(method="execute",at=@At(value="INVOKE",target="gay/object/ioticblocks/casting/actions/OpWriteIndex$Spell.<init> (Lat/petrak/hexcasting/api/casting/iota/Iota;Lat/petrak/hexcasting/api/addldata/ADIotaHolder;)V"),index=0,remap = false)
    private Iota focalworks_new_list(Iota datum) {
        if (!is_specialised && hex != null) {
            HashMap<String, Object> map = Focalworks.CONTEXT.get();
            CastingVM vm = (CastingVM) map.get("vm");
            List<Iota> stack = vm.getImage().getStack();
            stack.remove(stack.size()-1);
            stack.add(datum);
            vm.setImage(RiggedHexFinder.set_image_stack(vm.getImage(),stack));
            Pair<Iota, Boolean> result = RiggedHexFinder.cast_rigged_write_with_cancel(vm,hex);
            isNotCancelled = result.getSecond();
            return result.getFirst();
        }
        return datum;
    }
    @Inject(method="execute",at=@At(value="TAIL"),cancellable = true,remap = false)
    @Unique
    private void focalworks_canceller(List<Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        if (!isNotCancelled) {
            cir.setReturnValue(new SpellAction.Result(
                    Focalworks.emptyRenderedSpell,
                    0L,
                    List.of(),
                    1L
            ));
        }
    }
}
