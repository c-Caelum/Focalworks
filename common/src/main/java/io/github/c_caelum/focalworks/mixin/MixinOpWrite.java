package io.github.c_caelum.focalworks.mixin;


import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem;
import at.petrak.hexcasting.common.casting.actions.rw.OpWrite;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;


@Mixin(value = OpWrite.class, remap = false)
public class MixinOpWrite {
    @WrapMethod(method="operate")
    public OperationResult focalworks_operate(
            CastingEnvironment env, CastingImage image, SpellContinuation continuation, Operation<OperationResult> original
    ) {
        CastingImage img;
        SpellContinuation cont;
        List<Iota> stack = image.getStack();
        Iota datum = stack.get(0);
        CastingVM vm = new CastingVM(image,env);
        CastingEnvironment.HeldItemInfo info = env.getHeldItemToOperateOn(it ->  {
            ADIotaHolder dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it);
            return dataHolder != null && dataHolder.writeIota(datum, true);});
        if (info == null) {
            info = env.getHeldItemToOperateOn(it -> {
                ADIotaHolder dataHolder = IXplatAbstractions.INSTANCE.findDataHolder(it);
                return dataHolder != null;
            });
            if (info == null) {throw MishapBadOffhandItem.of(null,"iota.write");}
        }

        ADIotaHolder datumHolder = IXplatAbstractions.INSTANCE.findDataHolder(info.stack());
        if (datumHolder == null ) {throw MishapBadOffhandItem.of(info.stack(),"iota.write");}
        ListIota hex =  (ListIota) IotaType.deserialize((CompoundTag) info.stack().getTag().get("riggedwrite"),env.getWorld());
        hex.getList().forEach(iota -> vm.queueExecuteAndWrapIota(iota,env.getWorld()));
        img = vm.getImage();
        cont = continuation;
        return original.call(env,img,cont);

    }
}
