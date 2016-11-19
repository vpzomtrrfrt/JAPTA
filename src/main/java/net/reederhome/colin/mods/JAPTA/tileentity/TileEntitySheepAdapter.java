package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class TileEntitySheepAdapter extends TileEntityJPT implements ITickable, IEnergyReceiver, IEnergyProvider {
    public static int MAX_SHEEP_AMOUNT = 8000;
    public static int RANGE = 4;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return MAX_SHEEP_AMOUNT;
    }


    @Override
    public void update() {
        stored += equalize(getWorld(), stored, null, getPos());
        transmit();
    }

    private static int equalize(World world, int stored, EntitySheep sheep, BlockPos blockPos) {
        double[] pos;
        if (sheep == null) {
            pos = new double[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
        } else {
            pos = new double[]{sheep.posX, sheep.posY, sheep.posZ};
            if(sheep.getEntityData().getInteger("Energy") == 0) {
                return 0;
            }
        }
        List<EntitySheep> sheepList = world.getEntitiesWithinAABB(EntitySheep.class, new AxisAlignedBB(pos[0] - RANGE, pos[1] - RANGE, pos[2] - RANGE, pos[0] + RANGE, pos[1] + RANGE, pos[2] + RANGE));
        int currentStored = stored;
        for (EntitySheep curSheep : sheepList) {
            if(curSheep != sheep) {
                NBTTagCompound tag = curSheep.getEntityData();
                int energy = tag.getInteger("Energy");
                int tr = (currentStored - energy) / 2;
                tag.setInteger("Energy", energy + tr);
                currentStored -= tr;
            }
        }
        return currentStored-stored;
    }
    public static void equalize(EntitySheep sheep) {
        int stored = sheep.getEntityData().getInteger("Energy");
        sheep.getEntityData().setInteger("Energy", stored+equalize(sheep.getEntityWorld(), stored, sheep, null));
    }
}
