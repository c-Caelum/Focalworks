package caelum.focalworks.casting.actions;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import static at.petrak.hexcasting.api.casting.OperatorUtils.getItemEntity;
import static at.petrak.hexcasting.api.casting.OperatorUtils.getList;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpRigWrite implements ConstMediaAction {
    private static final Integer argc = 2;
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
        Iota hex = new ListIota(getList(args,1,argc));
        ItemStack stack = entity.getItem();
        CompoundTag tag = IotaType.serialize(hex);
        NBTHelper.put(stack,"riggedwrite",tag);
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
