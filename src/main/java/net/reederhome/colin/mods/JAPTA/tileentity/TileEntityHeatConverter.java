package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.BlockFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.EnumConverterMode;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockConverter;

public class TileEntityHeatConverter extends TileEntityJPT implements IEnergyReceiver, IEnergyProvider, ITickable {
    public static final int USE = 20;

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 4000;
    }

    @Override
    public void update() {
        BlockPos dest = getPos().down();
        TileEntity te = worldObj.getTileEntity(dest);
        if (te instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) te;
            EnumConverterMode mode = JAPTA.safeGetValue(worldObj.getBlockState(getPos()), BlockConverter.MODE);
            if (mode == EnumConverterMode.ABSORB) {
                if(stored > 0) {
                    transmit();
                }
                if (getMaxEnergyStored(null) >= stored + USE) {
                    if (furnace.isBurning()) {
                        stored += USE;
                    }
                    else {
                        ItemStack stack = furnace.getStackInSlot(1);
                        int burnTime = TileEntityFurnace.getItemBurnTime(stack);
                        if(burnTime > 0) {
                            stack.stackSize--;
                            if(stack.stackSize < 1) {
                                furnace.setInventorySlotContents(1, stack.getItem().getContainerItem(stack));
                            }
                            furnace.setField(0, burnTime);
                            BlockFurnace.setState(true, worldObj, dest);
                        }
                    }
                }
            } else {
                if (furnace.isBurning()) {
                    if (JAPTA.canSmelt(furnace) && furnace.getField(0) < 2 && stored >= USE) {
                        furnace.setField(0, furnace.getField(0) + 1);
                        stored -= USE;
                    }
                } else {
                    if (JAPTA.canSmelt(furnace) && stored >= USE * furnace.getCookTime(furnace.getStackInSlot(0))) {
                        furnace.setField(0, 2);
                        stored -= USE;
                        BlockFurnace.setState(true, worldObj, dest);
                    }
                }
            }
        }
    }

}
