package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockPowerCabinet;

public class TileEntityPowerCabinetBase extends TileEntity implements IEnergyReceiver, IEnergyProvider {
    private int stored = 0;

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        BlockPos cp = getPos();
        int leftToExtract = maxExtract;
        if (leftToExtract > stored) {
            leftToExtract -= stored;
            stored = 0;
        } else {
            stored -= leftToExtract;
            return leftToExtract;
        }
        while (true) {
            BlockPos np = cp.up();
            if (worldObj.getBlockState(np).getBlock() instanceof BlockPowerCabinet) {
                cp = np;
            } else {
                break;
            }
        }
        while (leftToExtract > 0) {
            IBlockState state = worldObj.getBlockState(cp);
            if (state.getBlock() instanceof BlockPowerCabinet) {
                int v = state.getValue(BlockPowerCabinet.VALUE);
                while (v > 0 && leftToExtract > 0) {
                    v--;
                    leftToExtract -= ((BlockPowerCabinet) state.getBlock()).getMetaValue();
                }
                worldObj.setBlockState(cp, state.withProperty(BlockPowerCabinet.VALUE, v));
                cp = cp.down();
            } else {
                break;
            }
        }
        if (leftToExtract < 0) {
            stored -= leftToExtract;
            return maxExtract;
        } else {
            return maxExtract - leftToExtract;
        }
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int leftToAdd = maxReceive + stored;
        BlockPos cp = getPos().up();
        while(leftToAdd >= BlockPowerCabinet.MAX_META_VALUE) {
            IBlockState state = worldObj.getBlockState(cp);
            if(state.getBlock() instanceof BlockPowerCabinet) {
                int v = state.getValue(BlockPowerCabinet.VALUE);
                while(v < 15 && leftToAdd >= BlockPowerCabinet.MAX_META_VALUE) {
                    v++;
                    leftToAdd -= ((BlockPowerCabinet) state.getBlock()).getMetaValue();
                }
                worldObj.setBlockState(cp, state.withProperty(BlockPowerCabinet.VALUE, v));
            }
            else {
                break;
            }
            cp = cp.up();
        }
        if(leftToAdd >= BlockPowerCabinet.MAX_META_VALUE) {
            stored = getMaxInternalStorage();
            return maxReceive + getMaxInternalStorage() - leftToAdd;
        }
        else {
            stored = leftToAdd;
            return maxReceive;
        }
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        int tr = stored;
        BlockPos cp = getPos().up();
        while (true) {
            IBlockState state = worldObj.getBlockState(cp);
            if (state.getBlock() instanceof BlockPowerCabinet) {
                tr += state.getValue(BlockPowerCabinet.VALUE) * ((BlockPowerCabinet) state.getBlock()).getMetaValue();
            } else {
                break;
            }
            cp = cp.up();
        }
        return tr;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        int tr = 0;
        BlockPos cp = getPos();
        while (true) {
            cp = cp.up();
            IBlockState state = worldObj.getBlockState(cp);
            if (state.getBlock() instanceof BlockPowerCabinet) {
                tr += ((BlockPowerCabinet) state.getBlock()).getMetaValue()*15;
            } else {
                break;
            }
        }
        return tr + getMaxInternalStorage();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("Energy", stored);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stored = compound.getInteger("Energy");
    }

    public int getMaxInternalStorage() {
        return BlockPowerCabinet.MAX_META_VALUE - 1;
    }
}
