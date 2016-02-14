package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.block.BlockFluxHopper;

public class TileEntityFluxHopper extends TileEntityJPT implements IEnergyReceiver, IEnergyProvider, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 60000;
    }

    @Override
    public void update() {
        int remaining = getMaxEnergyStored(null) - stored;
        if (remaining > 0) {
            TileEntity above = worldObj.getTileEntity(getPos().up());
            if (above instanceof IEnergyProvider) {
                stored += ((IEnergyProvider) above).extractEnergy(EnumFacing.DOWN, remaining, false);
            }
        }
        if (stored > 0) {
            EnumFacing facing = worldObj.getBlockState(getPos()).getValue(BlockFluxHopper.FACING);
            TileEntity target = worldObj.getTileEntity(getPos().offset(facing));
            if (target instanceof IEnergyReceiver) {
                stored -= ((IEnergyReceiver) target).receiveEnergy(facing.getOpposite(), stored, false);
            }
        }
    }
}
