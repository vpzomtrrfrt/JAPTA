package net.reederhome.colin.mods.JAPTA.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemJPT extends Item implements IEnergyContainerItem {
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
}
