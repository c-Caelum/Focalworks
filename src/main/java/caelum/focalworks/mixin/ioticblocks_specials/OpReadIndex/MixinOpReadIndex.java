package caelum.focalworks.mixin.ioticblocks_specials.OpReadIndex;


import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.utils.HexUtils;
import caelum.focalworks.Focalworks;
import caelum.focalworks.api.RiggedHexFinder;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import gay.object.ioticblocks.casting.actions.OpReadIndex;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static at.petrak.hexcasting.api.casting.iota.IotaType.deserialize;
import java.util.HashMap;
import java.util.List;

import static at.petrak.hexcasting.api.casting.iota.IotaType.deserialize;
import static at.petrak.hexcasting.api.casting.iota.IotaType.serialize;


// intellij is wrong, this is actually okay
// I don't know why it's not finding my expression, but launching works fine
@Mixin(value = OpReadIndex.class,remap = true)
public class MixinOpReadIndex {
    @Unique
    private SpellList hex = null;
    @Unique
    private boolean is_specialised = true;


    @Definition(id = "deserialize", method = "Lat/petrak/hexcasting/api/casting/iota/IotaType;deserialize(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/server/level/ServerLevel;)Lat/petrak/hexcasting/api/casting/iota/Iota;")
    @Definition(id = "env", local = @Local(type = CastingEnvironment.class, argsOnly = true))
    @Definition(id = "getWorld", method = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;getWorld()Lnet/minecraft/server/level/ServerLevel;")
    @Definition(id = "listOf", method = "Lkotlin/collections/CollectionsKt;listOf(Ljava/lang/Object;)Ljava/util/List;")
    @Definition(id = "datum", local = @Local(type = Iota.class, index=10))
    @Expression("return listOf(datum)")
    @Inject(method = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.BEFORE))
    private void focalworks_execute(
            List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (is_specialised && hex != null) {
            HashMap<String, Object> map = Focalworks.CONTEXT.get();
            SpellContinuation cont = (SpellContinuation) map.get("cont");
            FrameEvaluate frame = new FrameEvaluate(hex,false);
            map.put("cont",cont.pushFrame(frame));
        }
    }

    @Definition(id = "checkNotNull", method = "Lkotlin/jvm/internal/Intrinsics;checkNotNull(Ljava/lang/Object;)V")
    @Definition(id = "listTag", local = @Local(type = ListTag.class, index=8))
    //@Expression("(Tag)getOrNull((List)listTag,index)")

    @Expression("checkNotNull(listTag)")
    @ModifyVariable(method = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER), name = "listTag")
    private ListTag focalworks_new_list(ListTag listTag, /*CallbackInfoReturnable<List<Iota>> cir,
                                     /*@Local(type = ListTag.class) LocalRef<ListTag> listTag,*/
                                     @Local(type=CastingEnvironment.class,argsOnly = true) CastingEnvironment env,
                                     @Local(type = Either.class,name="target") Either<Entity,BlockPos> target) {
        if (target.right().isPresent()) {
            hex = RiggedHexFinder.get_rig_vec(target.right().get(),env.getWorld(),"riggedreadindex");
            if (hex == null) {
                is_specialised = false;
                hex = RiggedHexFinder.get_rig_vec(target.right().get(),env.getWorld(),"riggedread");
            }
        } else {
            Entity entity = target.left().get();
            if (entity instanceof ItemEntity) {
                hex = RiggedHexFinder.get_rig_item(((ItemEntity) entity).getItem(),env.getWorld(),"riggedreadindex");
                if (hex == null) {
                    is_specialised = false;
                    hex = RiggedHexFinder.get_rig_item(((ItemEntity) entity).getItem(),env.getWorld(),"riggedread");
                }
            }
        }

        if (!is_specialised && hex != null) {
            List<Iota> temp = new java.util.ArrayList<Iota>();
            listTag.forEach(tag ->{
                temp.add(deserialize((CompoundTag) tag,env.getWorld()));
            });
            HashMap<String, Object> map = Focalworks.CONTEXT.get();
            CastingVM vm = (CastingVM) map.get("vm");
            List<Iota> stack = vm.getImage().getStack();
            stack.remove(stack.size()-1);
            stack.remove(stack.size()-1);
            stack.add(new ListIota(temp));
            vm.setImage(RiggedHexFinder.set_image_stack(vm.getImage(),stack));
            Iota result = RiggedHexFinder.cast_rigged_read(vm,hex);
            map.put("vm", vm);
            Focalworks.CONTEXT.set(map);
            return HexUtils.downcast(result.serialize(),ListTag.TYPE);
        }
        return listTag;
    }
}
