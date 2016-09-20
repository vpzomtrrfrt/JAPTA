package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public abstract class TileEntityJPT extends TileEntityJPTBase implements ICapabilityProvider {
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
        if (!canReceiveEnergy(from)) {
            return 0;
        }
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

    public boolean canReceiveEnergy(EnumFacing from) {
        return this instanceof IEnergyReceiver;
    }

    public boolean canTransmitEnergy(EnumFacing from) {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setInteger("Energy", stored);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        stored = tag.getInteger("Energy");
    }

    public void transmit(EnumFacing side) {
        if (!canTransmitEnergy(side)) {
            return;
        }
        TileEntity te = worldObj.getTileEntity(getPos().offset(side));
        if (te instanceof IEnergyReceiver) {
            stored -= ((IEnergyReceiver) te).receiveEnergy(side.getOpposite(), stored, false);
        } else if (te != null && te.hasCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, side)) {
            stored -= te.getCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, side).givePower(stored, false);
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

    public void chargeItem(ItemStack stack) {
        if (stored > 0 && stack != null) {
            if (stack.getItem() instanceof IEnergyContainerItem) {
                stored -= ((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack, stored, false);
            }
            if (stack.hasCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, null)) {
                stored -= stack.getCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, null).givePower(stored, false);
            }
            if(stack.hasCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, null)) {
                stored -= stack.getCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, null).receiveEnergy(stored, false);
            }
        }
    }
}
