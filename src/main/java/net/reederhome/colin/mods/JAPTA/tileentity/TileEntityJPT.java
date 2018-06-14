package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.item.ItemJPT;

public abstract class TileEntityJPT extends TileEntityJPTBase implements ICapabilityProvider, TileEntityJPTBase.EnergyHolder {
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
        return this instanceof EnergyReceiver;
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
        TileEntity te = getWorld().getTileEntity(getPos().offset(side));
        stored -= JAPTA.receiveEnergy(te, side.getOpposite(), stored);
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
        stored -= chargeItem(stack, stored);
    }

    public static int chargeItem(ItemStack stack, int maxAmount) {
        int stored = maxAmount;
        if (stored > 0 && stack != null) {
            if (stack.getItem() instanceof ItemJPT) {
                stored -= ((ItemJPT) stack.getItem()).receiveEnergy(stack, stored, false);
            }
            try {
                if (JAPTA.CAPABILITY_TESLA_CONSUMER != null && stack.hasCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, null)) {
                    stored -= stack.getCapability(JAPTA.CAPABILITY_TESLA_CONSUMER, null).givePower(stored, false);
                }
                if (JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE != null && stack.hasCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, null)) {
                    stored -= stack.getCapability(JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE, null).receiveEnergy(stored, false);
                }
            } catch(NoClassDefFoundError ex) {
                // class not present, can't charge, and that's probably okay
            }
        }
        return maxAmount-stored;
    }

    public interface EnergyReceiver {
        int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate);
    }

    public interface EnergyProvider {
        int extractEnergy(EnumFacing from, int maxExtract, boolean simulate);
    }

}
