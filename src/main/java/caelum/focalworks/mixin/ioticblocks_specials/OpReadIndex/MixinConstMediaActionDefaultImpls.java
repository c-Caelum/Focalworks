package caelum.focalworks.mixin.ioticblocks_specials.OpReadIndex;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import caelum.focalworks.Focalworks;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gay.object.ioticblocks.casting.actions.OpReadIndex;
import gay.object.ioticblocks.casting.actions.OpWriteIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(ConstMediaAction.DefaultImpls.class)
public final class MixinConstMediaActionDefaultImpls {
    @Definition(id = "executeWithOpCount", method = "Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction;executeWithOpCount(Ljava/util/List;Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction$CostMediaActionResult;")
    @Expression("? = ?.executeWithOpCount(?,?)")
    @Inject(method="operate",at= @At(value = "MIXINEXTRAS:EXPRESSION", shift=At.Shift.BEFORE))
    private static void operate0(ConstMediaAction $this, CastingEnvironment env, CastingImage image, SpellContinuation continuation, CallbackInfoReturnable<OperationResult> cir) {
        if ($this instanceof OpReadIndex) {
            HashMap<String,Object> map = Focalworks.CONTEXT.get();
            map.put("vm",new CastingVM(image,env));
            map.put("cont",continuation);
            Focalworks.CONTEXT.set(map);
        }
    }

    @Definition(id = "executeWithOpCount", method = "Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction;executeWithOpCount(Ljava/util/List;Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction$CostMediaActionResult;")
    @Expression("? = ?.executeWithOpCount(?,?)")
    @Inject(method = "operate", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    private static void operate1(ConstMediaAction $this, CastingEnvironment environment, CastingImage castingImage, SpellContinuation spellContinuation, CallbackInfoReturnable<OperationResult> cir, @Local LocalRef<CastingEnvironment> env, @Local LocalRef<CastingImage> image, @Local LocalRef<SpellContinuation> continuation) {
        if ($this instanceof OpReadIndex) {
            HashMap<String,Object> map = Focalworks.CONTEXT.get();
            CastingVM vm = (CastingVM) map.get("vm");
            env.set(vm.getEnv());
            CastingImage img = vm.getImage();
            image.set(img.copy(img.getStack(), img.getParenCount(), img.getParenthesized(),img.getEscapeNext(),img.getOpsConsumed() + 2, img.getUserData()));
            continuation.set((SpellContinuation) map.get("cont"));
        }
    }
}
