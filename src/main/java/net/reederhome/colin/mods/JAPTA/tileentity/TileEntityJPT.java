package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityJPT extends TileEntity {
    public int stored = 0;

    public int getEnergyStored(EnumFacing from) {
        return stored;
    }

    public abstract int getMaxEnergyStored(EnumFacing from);

    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int tr;
        if (stored > maxExtract) {
            tr = maxExtract;
        } else {
            tr = stored;
        }
        if (!simulate) {
            stored -= tr;
        }
        return tr;
    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int avail = getMaxEnergyStored(from) - getEnergyStored(from);
        int tr;
        if (maxReceive < avail) {
            tr = maxReceive;
        } else {
            tr = avail;
        }
        if (!simulate) {
            stored += tr;
        }
        return tr;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("Energy", stored);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        stored = tag.getInteger("Energy");
    }

    public void transmit(EnumFacing side) {
        TileEntity te = worldObj.getTileEntity(getPos().offset(side));
        if (te instanceof IEnergyReceiver) {
            stored -= ((IEnergyReceiver) te).receiveEnergy(side.getOpposite(), stored, false);
        }
    }

    public void transmit(int side) {
        transmit(EnumFacing.VALUES[side]);
    }

    public void transmit() {
        for (int i = 0; i < 6; i++) {
            if (stored == 0) {
                break;
            }
            transmit(i);
        }
    }
}
