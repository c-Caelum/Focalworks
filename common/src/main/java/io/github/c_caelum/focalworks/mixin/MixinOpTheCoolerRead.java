package io.github.c_caelum.focalworks.mixin;


import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.common.casting.actions.rw.OpTheCoolerRead;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Mixin(OpTheCoolerRead.class)
public class MixinOpTheCoolerRead {
    @Inject(method="operate",at=@At("RETURN"),remap=false,cancellable = true)
    private void Focalworks_OpRead(
            CastingEnvironment env,
            CastingImage image,
            SpellContinuation continuation,
            CallbackInfoReturnable<OperationResult> cir
    ) {

    }

}
