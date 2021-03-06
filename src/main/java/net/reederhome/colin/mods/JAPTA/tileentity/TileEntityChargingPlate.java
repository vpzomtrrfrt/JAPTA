package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.JAPTA;

import java.util.List;

public class TileEntityChargingPlate extends TileEntityJPT implements TileEntityJPT.EnergyReceiver, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 1000;
    }

    @Override
    public void update() {
        List<EntityPlayer> l = JAPTA.chargingPlate.getPlayers(getWorld(), getPos());
        for (EntityPlayer p : l) {
            IInventory inv = p.inventory;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                chargeItem(stack);
            }
        }
    }
}
