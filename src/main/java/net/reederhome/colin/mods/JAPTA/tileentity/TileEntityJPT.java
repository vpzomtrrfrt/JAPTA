package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public abstract class TileEntityJPT extends TileEntity implements ICapabilityProvider {
    public int stored = 0;

    @CapabilityInject(ITeslaHolder.class)
    private static Capability<ITeslaHolder> CAPABILITY_TESLA_HOLDER;
    @CapabilityInject(ITeslaProducer.class)
    private static Capability<ITeslaProducer> CAPABILITY_TESLA_PRODUCER;
    @CapabilityInject(ITeslaConsumer.class)
    private static Capability<ITeslaConsumer> CAPABILITY_TESLA_CONSUMER;

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
        if(!canReceiveEnergy(from)) {
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
        if(!canTransmitEnergy(side)) {
            return;
        }
        TileEntity te = worldObj.getTileEntity(getPos().offset(side));
        if (te instanceof IEnergyReceiver) {
            stored -= ((IEnergyReceiver) te).receiveEnergy(side.getOpposite(), stored, false);
        }
        else if(te != null && te.hasCapability(CAPABILITY_TESLA_CONSUMER, side)) {
            stored -= te.getCapability(CAPABILITY_TESLA_CONSUMER, side).givePower(stored, false);
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

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CAPABILITY_TESLA_HOLDER || capability == CAPABILITY_TESLA_CONSUMER || capability == CAPABILITY_TESLA_PRODUCER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CAPABILITY_TESLA_HOLDER || capability == CAPABILITY_TESLA_CONSUMER || capability == CAPABILITY_TESLA_PRODUCER) {
            return (T) new JPTTeslaAdapter(facing);
        }
        return super.getCapability(capability, facing);
    }

    private class JPTTeslaAdapter implements ITeslaHolder, ITeslaConsumer, ITeslaProducer {
        private EnumFacing facing;

        public JPTTeslaAdapter(EnumFacing facing) {
            this.facing = facing;
        }

        @Override
        public long givePower(long power, boolean simulated) {
            return receiveEnergy(facing, (int) power, simulated);
        }

        @Override
        public long getStoredPower() {
            return getEnergyStored(facing);
        }

        @Override
        public long getCapacity() {
            return getMaxEnergyStored(facing);
        }

        @Override
        public long takePower(long power, boolean simulated) {
            return extractEnergy(facing, (int) power, simulated);
        }
    }
}
