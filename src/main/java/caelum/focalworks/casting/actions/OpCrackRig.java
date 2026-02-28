package caelum.focalworks.casting.actions;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import static at.petrak.hexcasting.api.casting.OperatorUtils.getItemEntity;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.NBTHelper;
import caelum.focalworks.casting.mishaps.MishapAlreadyRigged;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpCrackRig implements ConstMediaAction {
    private static final Integer argc = 1;
    @Override
    public int getArgc() {
        return argc;
    }
    @Override
    public long getMediaCost() {
        return 20 * MediaConstants.DUST_UNIT;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        ItemEntity entity = getItemEntity(args,0,argc);
        ItemStack stack = entity.getItem();
        if(NBTHelper.contains(stack,"riggedread") || NBTHelper.contains(stack,"riggedwrite")) {
            throw new MishapAlreadyRigged();
        } else {
            NBTHelper.putInt(stack,"cracked_rig",1);
        }
        return List.of();
    }

    @Override
    public @NotNull CostMediaActionResult executeWithOpCount(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        return DefaultImpls.executeWithOpCount(this,args,env);
    }

    @Override
    public @NotNull OperationResult operate(@NotNull CastingEnvironment env, @NotNull CastingImage image, @NotNull SpellContinuation continuation) {
        return DefaultImpls.operate(this,env,image,continuation);
    }
}
