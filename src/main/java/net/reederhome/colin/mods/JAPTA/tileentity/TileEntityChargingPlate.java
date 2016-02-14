package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

import java.util.List;

public class TileEntityChargingPlate extends TileEntityJPT implements IEnergyReceiver, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 1000;
    }

    @Override
    public void update() {
        List<EntityPlayer> l = JAPTA.chargingPlate.getPlayers(worldObj, getPos());
        for (EntityPlayer p : l) {
            IInventory inv = p.inventory;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    Item item = stack.getItem();
                    if (item instanceof IEnergyContainerItem) {
                        if (stored > 0) {
                            stored -= ((IEnergyContainerItem) item).receiveEnergy(stack, stored, false);
                        }
                    }
                }
            }
        }
    }
}
