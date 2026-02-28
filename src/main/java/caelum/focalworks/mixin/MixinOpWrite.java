package caelum.focalworks.mixin;


import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem;
import at.petrak.hexcasting.common.casting.actions.rw.OpWrite;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import caelum.focalworks.casting.mishaps.MishapInternalHex;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

// gosh, I think I'm going to explode on the spot
// practically reimplementing execute and parts of operate, but I don't know a better way - make an issue if you
// have a better idea
@Mixin(value = OpWrite.class, remap = false)
public class MixinOpWrite {
    @WrapMethod(method = "operate")
    public OperationResult focalworks_operate(
            CastingEnvironment env, CastingImage image, SpellContinuation continuation, Operation<OperationResult> original
    ) {
        CastingImage img = image;
        SpellContinuation cont = continuation;
        List<Iota> stack = image.getStack();
        Iota datum = stack.get(0);
        CastingVM vm = new CastingVM(image, env);
        CastingEnvironment.HeldItemInfo info = env.getHeldItemToOperateOn(it -> {
            ADIotaHolder dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it);
            return dataHolder != null && dataHolder.writeIota(datum, true);
        });
        if (info == null) {
            info = env.getHeldItemToOperateOn(it -> {
                ADIotaHolder dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it);
                return dataHolder != null;
            });
            if (info == null) {
                throw MishapBadOffhandItem.of(null, "iota.write");
            }
        }

        ADIotaHolder datumHolder = IXplatAbstractions.INSTANCE.findDataHolder(info.stack());
        if (datumHolder == null) {
            throw MishapBadOffhandItem.of(info.stack(), "iota.write");
        }
        if (info.stack().getOrCreateTag().contains("riggedwrite")) {
            ListIota hex = (ListIota) IotaType.deserialize((CompoundTag) info.stack().getTag().get("riggedwrite"), env.getWorld());
            List<Iota> temp = new ArrayList<Iota>();
            hex.getList().forEach(temp::add);
            ExecutionClientView result = vm.queueExecuteAndWrapIotas(temp, env.getWorld());
            if (result.getResolutionType() == ResolvedPatternType.ERRORED) {
                throw new MishapInternalHex();
            }
            img = vm.getImage();
            stack = img.getStack();
            if (!stack.isEmpty()) {
                Iota last = stack.get(stack.size()-1);
                if (last instanceof BooleanIota) {
                    stack.remove(stack.size()-1);

                    img = img.copy(stack,img.getParenCount(),img.getParenthesized(),img.getEscapeNext(),img.getOpsConsumed(),img.getUserData());
                    if (!((BooleanIota)last).getBool()) {
                        List<OperatorSideEffect> sideEffects = new ArrayList<OperatorSideEffect>();
                        return new OperationResult(img,sideEffects,continuation, HexEvalSounds.MUTE);
                    }
                }
            }
            cont = continuation;
        }

        return original.call(env, img, cont);

    }
}

