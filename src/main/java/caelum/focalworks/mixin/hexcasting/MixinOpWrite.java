package caelum.focalworks.mixin.hexcasting;

import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.common.casting.actions.rw.OpWrite;
import caelum.focalworks.Focalworks;
import caelum.focalworks.api.RiggedHexFinder;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
// yayayayayayayay
@Mixin(value = OpWrite.class, remap = false)
public class MixinOpWrite {
    @Unique
    private static Boolean isCancelled = true;

    @Definition(id = "component1", method = "at/petrak/hexcasting/api/casting/eval/CastingEnvironment$HeldItemInfo.component1 ()Lnet/minecraft/world/item/ItemStack;")
    @Expression("? = ?.component1()")
    @Unique
    //@ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/common/casting/actions/rw/OpWrite$Spell;<init>(Lat/petrak/hexcasting/api/casting/iota/Iota;Lat/petrak/hexcasting/api/addldata/ADIotaHolder;)V"))
    @ModifyVariable(method = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER),name="datum")
    private Iota focalworks_execute(Iota value, @Local(name = "handStack") ItemStack handStack, @Local(name="env")CastingEnvironment env) {
        HashMap<String,Object> map = Focalworks.CONTEXT.get();
        CastingVM vm = (CastingVM)map.get("vm");
        RiggedHexFinder.cast_rigged_hex(vm,RiggedHexFinder.get_rig_item(handStack, env.getWorld(),"riggedwrite"));
        CastingImage image = vm.getImage();
        List<Iota> stack = image.getStack();
        Iota top = stack.remove(stack.size()-1);

        vm.setImage(image.copy(
                stack,
                image.getParenCount(),
                image.getParenthesized(),
                image.getEscapeNext(),
                image.getOpsConsumed(),
                image.getUserData()
        ));
        if (top instanceof BooleanIota) {
            isCancelled = ((BooleanIota) top).getBool();
            if (isCancelled) {
                return stack.remove(stack.size()-1);
            } else {/* I don't really need this, but it's just to make sure no errors pop up */ return new NullIota();}
        } else {
            return top;
        }
    }
    @Inject(method="execute",at=@At(value="TAIL"),cancellable = true)
    private void focalworks_canceller(List<Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        if (!isCancelled) {
            cir.setReturnValue(new SpellAction.Result(
                    Focalworks.emptyRenderedSpell,
                    0L,
                    List.of(),
                    1L
            ));
        }
    }
}