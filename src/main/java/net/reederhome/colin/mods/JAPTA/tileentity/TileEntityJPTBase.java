package net.reederhome.colin.mods.JAPTA.tileentity;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import vazkii.botania.api.mana.IManaReceiver;

@Optional.Interface(iface="vazkii.botania.api.mana.IManaReceiver", modid="Botania")
public class TileEntityJPTBase extends TileEntity implements ICapabilityProvider, IManaReceiver {
    public static final int MANA_CONVERSION_RATE = 10;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == JAPTA.CAPABILITY_TESLA_HOLDER || capability == JAPTA.CAPABILITY_TESLA_CONSUMER || capability == JAPTA.CAPABILITY_TESLA_PRODUCER || capability == JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == JAPTA.CAPABILITY_TESLA_HOLDER || capability == JAPTA.CAPABILITY_TESLA_CONSUMER || capability == JAPTA.CAPABILITY_TESLA_PRODUCER) {
            return (T) new JPTTeslaAdapter(facing, this);
        } else if (capability == JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE) {
            return (T) new JPTForgeEnergyAdapter(facing, this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean isFull() {
        return !(this instanceof TileEntityJPT.EnergyHolder) || ((TileEntityJPT.EnergyHolder) this).getEnergyStored(null) >= ((TileEntityJPT.EnergyHolder) this).getMaxEnergyStored(null);
    }

    @Override
    public void recieveMana(int mana) {
        if (this instanceof TileEntityJPT.EnergyHolder) {
            ((TileEntityJPT.EnergyReceiver) this).receiveEnergy(null, mana * MANA_CONVERSION_RATE, false);
        }
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        if (this instanceof TileEntityJPT.EnergyHolder) {
            return ((TileEntityJPT.EnergyHolder) this).getEnergyStored(null) / MANA_CONVERSION_RATE;
        }
        return 0;
    }

    public interface EnergyHolder {
        int getEnergyStored(EnumFacing from);
        int getMaxEnergyStored(EnumFacing from);
    }

    public static class JPTTeslaAdapter implements ITeslaHolder, ITeslaConsumer, ITeslaProducer {
        private EnumFacing facing;
        private TileEntity te;

        public JPTTeslaAdapter(EnumFacing facing, TileEntity te) {
            this.facing = facing;
            this.te = te;
        }

        @Override
        public long givePower(long power, boolean simulated) {
            if(!(te instanceof TileEntityJPT.EnergyReceiver)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyReceiver) te).receiveEnergy(facing, (int) power, simulated);
        }

        @Override
        public long getStoredPower() {
            if(!(te instanceof TileEntityJPT.EnergyHolder)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyHolder) te).getEnergyStored(facing);
        }

        @Override
        public long getCapacity() {
            if(!(te instanceof TileEntityJPT.EnergyHolder)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyHolder) te).getMaxEnergyStored(facing);
        }

        @Override
        public long takePower(long power, boolean simulated) {
            if(!(te instanceof TileEntityJPT.EnergyProvider)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyProvider) te).extractEnergy(facing, (int) power, simulated);
        }
    }

    public static class JPTForgeEnergyAdapter implements IEnergyStorage {
        private EnumFacing facing;
        private TileEntity te;

        public JPTForgeEnergyAdapter(EnumFacing facing, TileEntity te) {
            this.facing = facing;
            this.te = te;
        }

        @Override
        public int receiveEnergy(int i, boolean b) {
            if(!(te instanceof TileEntityJPT.EnergyReceiver)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyReceiver) te).receiveEnergy(facing, i, b);
        }

        @Override
        public int extractEnergy(int i, boolean b) {
            if(!(te instanceof TileEntityJPT.EnergyProvider)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyProvider) te).extractEnergy(facing, i, b);
        }

        @Override
        public int getEnergyStored() {
            if(!(te instanceof TileEntityJPT.EnergyHolder)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyHolder) te).getEnergyStored(facing);
        }

        @Override
        public int getMaxEnergyStored() {
            if(!(te instanceof TileEntityJPT.EnergyHolder)) {
                return 0;
            }
            return ((TileEntityJPT.EnergyHolder) te).getMaxEnergyStored(facing);
        }

        @Override
        public boolean canExtract() {
            return te instanceof TileEntityJPT.EnergyProvider;
        }

        @Override
        public boolean canReceive() {
            return te instanceof TileEntityJPT.EnergyReceiver;
        }
    }
}
