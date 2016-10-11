package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyContainerItem;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import vazkii.botania.api.mana.IManaItem;

import static net.reederhome.colin.mods.JAPTA.tileentity.TileEntityJPTBase.MANA_CONVERSION_RATE;

@Optional.Interface(iface="vazkii.botania.api.mana.IManaItem", modid="botania")
public class ItemJPT extends Item implements IEnergyContainerItem, IManaItem {
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int remaining = getMaxEnergyStored(container) - getEnergyStored(container);
        int tr;
        if (maxReceive < remaining) {
            tr = maxReceive;
        } else {
            tr = remaining;
        }
        if (!simulate) {
            container.setItemDamage(container.getItemDamage() - tr);
        }
        return tr;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int tr;
        int stored = getEnergyStored(container);
        if (maxExtract < stored) {
            tr = maxExtract;
        } else {
            tr = stored;
        }
        if (!simulate) {
            container.setItemDamage(container.getItemDamage() + tr);
        }
        return tr;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return container.getMaxDamage() - container.getItemDamage();
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getMaxDamage(container);
    }

    public ItemJPT() {
        super();
        setMaxStackSize(1);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound tag) {
        return new CapabilityProvider(stack);
    }

    @Override
    public int getMana(ItemStack stack) {
        return getEnergyStored(stack)/MANA_CONVERSION_RATE;
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return getMaxEnergyStored(stack)/MANA_CONVERSION_RATE;
    }

    @Override
    public void addMana(ItemStack stack, int mana) {
        receiveEnergy(stack, mana*MANA_CONVERSION_RATE, false);
    }

    @Override
    public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
        return true;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return true;
    }

    @Override
    public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
        return false;
    }

    @Override
    public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
        return false;
    }

    @Override
    public boolean isNoExport(ItemStack stack) {
        return true;
    }

    public void setEnergyStored(ItemStack stack, int amount) {
        stack.setItemDamage(stack.getMaxDamage() - amount);
    }

    private class CapabilityProvider implements ICapabilityProvider

    {
        private ItemStack stack;

        public CapabilityProvider(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            if (capability == JAPTA.CAPABILITY_TESLA_HOLDER || capability == JAPTA.CAPABILITY_TESLA_CONSUMER || capability == JAPTA.CAPABILITY_TESLA_PRODUCER || capability == JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE) {
                return true;
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if (capability == JAPTA.CAPABILITY_TESLA_HOLDER || capability == JAPTA.CAPABILITY_TESLA_CONSUMER || capability == JAPTA.CAPABILITY_TESLA_PRODUCER) {
                return (T) new TeslaAdapter();
            } else if (capability == JAPTA.CAPABILITY_FORGE_ENERGY_STORAGE) {
                return (T) new ForgeEnergyAdapter();
            }
            return null;
        }

        private class TeslaAdapter implements ITeslaConsumer, ITeslaHolder, ITeslaProducer {

            @Override
            public long givePower(long power, boolean simulated) {
                return receiveEnergy(stack, (int) power, simulated);
            }

            @Override
            public long getStoredPower() {
                return getEnergyStored(stack);
            }

            @Override
            public long getCapacity() {
                return getMaxEnergyStored(stack);
            }

            @Override
            public long takePower(long power, boolean simulated) {
                return extractEnergy(stack, (int) power, simulated);
            }
        }

        private class ForgeEnergyAdapter implements IEnergyStorage {

            @Override
            public int receiveEnergy(int i, boolean b) {
                return ItemJPT.this.receiveEnergy(stack, i, b);
            }

            @Override
            public int extractEnergy(int i, boolean b) {
                return ItemJPT.this.extractEnergy(stack, i, b);
            }

            @Override
            public int getEnergyStored() {
                return ItemJPT.this.getEnergyStored(stack);
            }

            @Override
            public int getMaxEnergyStored() {
                return ItemJPT.this.getMaxEnergyStored(stack);
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return true;
            }
        }
    }

}
