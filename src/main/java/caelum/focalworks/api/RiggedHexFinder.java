package caelum.focalworks.api;

import at.petrak.hexcasting.api.casting.SpellList;
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RiggedHexFinder {
    public static SpellList get_rig_read_vec(BlockPos pos, ServerLevel world) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        assert blockEntity != null;
        CompoundTag tag = blockEntity.saveWithoutMetadata();
        if (NBTHelper.contains(tag,"riggedread")) {
            return ((ListIota) IotaType.deserialize((CompoundTag) NBTHelper.get(tag, "riggedread"), world)).getList();
        }

        if (blockEntity instanceof BaseContainerBlockEntity) {
            return get_rig_read_item(((BaseContainerBlockEntity)blockEntity).getItem(0),world);
        }

        return null;
    }
    public static SpellList get_rig_write_vec(BlockPos pos, ServerLevel world) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        assert blockEntity != null;
        CompoundTag tag = blockEntity.saveWithoutMetadata();
        if (NBTHelper.contains(tag,"riggedwrite")) {
            return ((ListIota) IotaType.deserialize((CompoundTag) NBTHelper.get(tag, "riggedwrite"), world)).getList();
        }

        if (blockEntity instanceof BaseContainerBlockEntity) {
            return get_rig_write_item(((BaseContainerBlockEntity)blockEntity).getItem(0),world);
        }

        return null;
    }
    public static SpellList get_rig_write_item(ItemStack item, ServerLevel world) {
        if(item!=null && world != null) {
            CompoundTag tag = item.getOrCreateTag();
            if (NBTHelper.contains(tag, "riggedwrite")) {
                ListIota hex = (ListIota) IotaType.deserialize((CompoundTag) NBTHelper.get(tag, "riggedwrite"), world);
                return hex.getList();
            }
        }
        return null;
    }
    public static SpellList get_rig_read_item(ItemStack item, ServerLevel world) {
        if (item != null && world != null) {
            CompoundTag tag = item.getOrCreateTag();
            if (NBTHelper.contains(tag, "riggedread")) {
                ListIota hex = (ListIota) IotaType.deserialize((CompoundTag) NBTHelper.get(tag, "riggedread"), world);
                return hex.getList();
            }
        }
        return null;
    }

}
