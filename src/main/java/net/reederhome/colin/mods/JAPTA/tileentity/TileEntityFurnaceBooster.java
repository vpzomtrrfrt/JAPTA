package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.reederhome.colin.mods.JAPTA.JAPTA;

public class TileEntityFurnaceBooster extends TileEntity implements ITickable {

    @Override
    public void update() {
        for (int i = 0; i < 6; i++) {
            EnumFacing side = EnumFacing.getFront(i);
            BlockPos cp = getPos().offset(side);
            TileEntity te = getWorld().getTileEntity(cp);
            if (te instanceof TileEntityFurnace) {
                TileEntityFurnace furnace = (TileEntityFurnace) te;
                if (furnace.isBurning() && (JAPTA.canSmelt(furnace) && furnace.getField(2) > 0 && furnace.getField(2) + 1 < furnace.getField(3) && furnace.getField(0) > 1)) {
                    furnace.setField(0, furnace.getField(0) - 1);
                    furnace.setField(2, furnace.getField(2) + 1);
                }
            }
        }
    }
}
