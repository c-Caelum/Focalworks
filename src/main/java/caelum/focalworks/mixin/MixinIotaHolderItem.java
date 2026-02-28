package caelum.focalworks.mixin;


import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(IotaHolderItem.class)
public interface MixinIotaHolderItem {
    @WrapMethod(method="appendHoverText",remap = true)
    private static void Focalworks_appendHoverText(IotaHolderItem self, ItemStack stack, List<Component> components, TooltipFlag flag, Operation<Void> original) {
        if (stack.hasTag() && stack.getOrCreateTag().contains("cracked_rig")){
            if (stack.hasTag() && stack.getOrCreateTag().contains("riggedread")) {
                Component hex = IotaType.getDisplay((CompoundTag) stack.getTag().get("riggedread"));
                components.add(Component.translatable("focalworks.riggedread.onitem",hex).withStyle(ChatFormatting.RED));
            }
            if (stack.hasTag() && stack.getOrCreateTag().contains("riggedwrite")) {
                Component hex = IotaType.getDisplay((CompoundTag) stack.getTag().get("riggedwrite"));
                components.add(Component.translatable("focalworks.riggedwrite.onitem",hex).withStyle(ChatFormatting.RED));
            }
        }
        original.call(self,stack,components,flag);
    }
}