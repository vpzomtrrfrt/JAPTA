package net.reederhome.colin.mods.JAPTA.block;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.tileentity.TileEntityJPT;

public class TileEntityFluxBlaster extends TileEntityJPT implements IEnergyReceiver, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 10000;
    }

    @Override
    public void update() {
        if(stored > 0) {
            EnumFacing facing = worldObj.getBlockState(getPos()).getValue(BlockBlaster.FACING);
            for(int i = 1; i < BlockBlaster.RANGE; i++) {
                BlockPos cp = getPos().offset(facing, i);
                while(worldObj.getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                    cp = cp.up();
                }
                TileEntity te = worldObj.getTileEntity(cp);
                if(te instanceof IEnergyReceiver && stored > 0) {
                    stored -= ((IEnergyReceiver) te).receiveEnergy(facing.getOpposite(), stored, false);
                }
            }
        }
    }
}
