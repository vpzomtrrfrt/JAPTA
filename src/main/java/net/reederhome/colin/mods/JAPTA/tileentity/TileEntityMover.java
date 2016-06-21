package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockMover;

import java.util.List;

public class TileEntityMover extends TileEntityJPT implements IEnergyReceiver, ITickable {
    public static int USE = 40;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 200;
    }

    @Override
    public void update() {
        long time = worldObj.getTotalWorldTime();
        BlockPos me = getPos();
        EnumFacing facing = JAPTA.safeGetValue(worldObj.getBlockState(me), BlockMover.FACING);
        transmit(facing);
        BlockPos front = me.offset(facing);
        List<Entity> l = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(me.getX(), me.getY()+1, me.getZ(), me.getX()+1, me.getY()+2, me.getZ()+1));
        l.addAll(worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(front.getX(), front.getY(), front.getZ(), front.getX()+1, front.getY()+1, front.getZ()+1)));
        for(Entity e : l) {
            if(stored >= USE && !e.isSneaking()) {
                NBTTagCompound ed = e.getEntityData();
                if(!(ed.hasKey("LastTeleported") && ed.getLong("LastTeleported")==time)) {
                    e.setPositionAndUpdate(e.posX + facing.getFrontOffsetX(), e.posY + facing.getFrontOffsetY(), e.posZ + facing.getFrontOffsetZ());
                    ed.setLong("LastTeleported", time);
                    stored -= USE;
                }
            }
        }
    }
}
