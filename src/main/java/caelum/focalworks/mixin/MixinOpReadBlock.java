package caelum.focalworks.mixin;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock;
import at.petrak.hexcasting.common.casting.actions.rw.OpTheCoolerRead;
import caelum.focalworks.api.RiggedHexFinder;
import gay.object.ioticblocks.api.IoticBlocksAPI;
import gay.object.ioticblocks.utils.IoticBlocksUtils;
import static at.petrak.hexcasting.api.casting.OperatorUtils.getBlockPos;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = OpTheCoolerRead.class,remap=false,priority=1001)
public class MixinOpReadBlock {
    @Shadow
    public int getArgc() {return 0;}

    @Unique
    private static SpellContinuation cont;

    @Inject(method="operate",at= @At(value = "INVOKE", target = "executeWithOpCount(Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction;Ljava/util/List;Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction$CostMediaActionResult;"))
    private static void operate(CastingEnvironment env, CastingImage image, SpellContinuation continuation, CallbackInfoReturnable<OperationResult> cir) {
        cont = continuation;
    }
    @Inject(method="operate",at= @At(value = "RETURN"), cancellable = true)
    private static void operate_after(CastingEnvironment env, CastingImage image, SpellContinuation continuation, CallbackInfoReturnable<OperationResult> cir) {
        cir.setReturnValue(new OperationResult(cir.getReturnValue().component1(),cir.getReturnValue().component2(),cont,cir.getReturnValue().component4()));
    }
    @Inject(method="execute",at= @At("HEAD"), cancellable = true, remap = false)
    private void execute(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        IoticBlocksUtils.getEntityOrBlockPos(args, 0, getArgc()).ifRight(target -> {
            env.assertPosInRange(target);
            ADIotaHolder datumHolder = IoticBlocksAPI.INSTANCE.findIotaHolder(env.getWorld(), target);
            if (datumHolder == null) {throw MishapBadBlock.of(target,"iota.read");}

            Iota datum = datumHolder.readIota(env.getWorld());
            if (datum == null) {
                datum = datumHolder.emptyIota();
            }
            if (datum == null) {
                throw MishapBadBlock.of(target,"iota.read");
            }
            SpellList hex = RiggedHexFinder.get_rig_read_vec(target,env.getWorld());
            if (hex != null) {
                FrameEvaluate frame = new FrameEvaluate(hex, true);
                cont.pushFrame(frame);
            }
            cir.setReturnValue(List.of(datum));
        });

    }
}
