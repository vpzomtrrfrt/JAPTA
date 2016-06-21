package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityChestCharger extends TileEntityJPT implements IEnergyReceiver, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 2000;
    }

    @Override
    public void update() {
        for(EnumFacing side : EnumFacing.VALUES) {
            if(stored > 0) {
                BlockPos cp = getPos().offset(side);
                TileEntity te = worldObj.getTileEntity(cp);
                if(te instanceof IInventory) {
                    IInventory inv = (IInventory) te;
                    for(int i = 0; i < inv.getSizeInventory(); i++) {
                        ItemStack stack = inv.getStackInSlot(i);
                        if(stack != null && stack.getItem() instanceof IEnergyContainerItem && stored > 0) {
                            stored -= ((IEnergyContainerItem) stack.getItem()).receiveEnergy(stack, stored, false);
                        }
                    }
                }
            }
        }
    }
}
