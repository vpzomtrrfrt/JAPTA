package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockFluxHopper;

public class TileEntityFluxHopper extends TileEntityJPT implements TileEntityJPT.EnergyReceiver, TileEntityJPT.EnergyProvider, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 6000;
    }

    @Override
    public void update() {
        int remaining = getMaxEnergyStored(null) - stored;
        if (remaining > 0) {
            TileEntity above = getWorld().getTileEntity(getPos().up());
            stored += JAPTA.extractEnergy(above, EnumFacing.DOWN, remaining);
        }
        if (stored > 0) {
            transmit(JAPTA.safeGetValue(getWorld().getBlockState(getPos()), BlockFluxHopper.FACING));
        }
    }

    @Override
    public boolean canReceiveEnergy(EnumFacing from) {
        return super.canReceiveEnergy(from) && from != JAPTA.safeGetValue(getWorld().getBlockState(getPos()), BlockFluxHopper.FACING);
    }
}
