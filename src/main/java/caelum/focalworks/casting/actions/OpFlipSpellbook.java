package caelum.focalworks.casting.actions;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.NBTHelper;

import static at.petrak.hexcasting.api.casting.OperatorUtils.*;
import static caelum.focalworks.Focalworks.clamp;

import at.petrak.hexcasting.common.items.storage.ItemSpellbook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpFlipSpellbook implements ConstMediaAction {
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
        int page = getIntBetween(args,1,1,64,argc);

        ItemStack stack = entity.getItem();
        if (!(stack.getItem() instanceof ItemSpellbook)) {
            throw new MishapBadItem(entity, Component.translatable("spellbook_not_empty"));
        }
        NBTHelper.putInt(stack,"page_idx",page);

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
