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

    public static final int META_VALUE = 1000;

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
            if (worldObj.getBlockState(np).getBlock() == JAPTA.powerCabinet) {
                cp = np;
            } else {
                break;
            }
        }
        while (leftToExtract > 0) {
            IBlockState state = worldObj.getBlockState(cp);
            if (state.getBlock() == JAPTA.powerCabinet) {
                int v = state.getValue(BlockPowerCabinet.VALUE);
                while (v > 0 && leftToExtract > 0) {
                    v--;
                    leftToExtract -= META_VALUE;
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
        while(leftToAdd >= META_VALUE) {
            IBlockState state = worldObj.getBlockState(cp);
            if(state.getBlock() == JAPTA.powerCabinet) {
                int v = state.getValue(BlockPowerCabinet.VALUE);
                while(v < 15 && leftToAdd >= META_VALUE) {
                    v++;
                    leftToAdd -= META_VALUE;
                }
                worldObj.setBlockState(cp, state.withProperty(BlockPowerCabinet.VALUE, v));
            }
            else {
                break;
            }
            cp = cp.up();
        }
        if(leftToAdd >= META_VALUE) {
            stored = META_VALUE - 1;
            return maxReceive + META_VALUE - 1 - leftToAdd;
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
            if (state.getBlock() == JAPTA.powerCabinet) {
                tr += state.getValue(BlockPowerCabinet.VALUE) * META_VALUE;
            } else {
                break;
            }
            cp = cp.up();
        }
        return tr;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        int c = 0;
        while (true) {
            IBlockState state = worldObj.getBlockState(getPos().up(c + 1));
            if (state.getBlock() == JAPTA.powerCabinet) {
                c++;
            } else {
                break;
            }
        }
        return c * META_VALUE * 15 + META_VALUE - 1;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Energy", stored);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stored = compound.getInteger("Energy");
    }
}
