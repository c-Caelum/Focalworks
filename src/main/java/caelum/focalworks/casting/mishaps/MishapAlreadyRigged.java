package caelum.focalworks.casting.mishaps;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MishapAlreadyRigged extends Mishap {
    private Component perp = null;
    private boolean is_crack = false;

    public MishapAlreadyRigged(EntityIota perpetrator,boolean is_cracking) {
        this.perp = perpetrator.display();
        this.is_crack = is_cracking;
    }
    public MishapAlreadyRigged(boolean is_cracking) {
        this.is_crack = is_cracking;
    }

    @Override
    public @NotNull FrozenPigment accentColor(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return dyeColor(DyeColor.LIME);
    }

    @Override
    public void execute(@NotNull CastingEnvironment env, @NotNull Mishap.Context context, @NotNull List<Iota> list) {
        LivingEntity caster = env.getCastingEntity();
        if (caster instanceof LivingEntity && !(caster instanceof ArmorStand)) {
            MobEffectInstance inst = new MobEffectInstance(MobEffects.BLINDNESS,200,1);
            caster.addEffect(inst);
        }
    }

    @Override
    protected @Nullable Component errorMessage(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        if (this.is_crack) {
            if(this.perp != null) {return error("already_rigged.crack.item",perp);}
            return error("already_rigged.crack");
        } else {
            if(this.perp != null) {return error("already_rigged.rigging.item",perp);}
            return error("already_rigged.rigging");
        }
    }
}
