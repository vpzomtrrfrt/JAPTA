package net.reederhome.colin.mods.JAPTA.tileentity;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;

public class TileEntityFluxBlaster extends TileEntityJPT implements IEnergyReceiver, IEnergyProvider, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 10000;
    }

    @Override
    public void update() {
        IBlockState state = worldObj.getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        for (int i = 1; i <= BlockBlaster.RANGE; i++) {
            BlockPos cp = getPos().offset(facing, i);
            while (worldObj.getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = worldObj.getTileEntity(cp);
            if(((BlockBlaster) state.getBlock()).isInhaler()) {
                int remaining = getMaxEnergyStored(null)-stored;
                if(te instanceof IEnergyProvider && remaining > 0) {
                    stored += ((IEnergyProvider) te).extractEnergy(facing.getOpposite(), remaining, false);
                }
            }
            else if (te instanceof IEnergyReceiver && stored > 0) {
                stored -= ((IEnergyReceiver) te).receiveEnergy(facing.getOpposite(), stored, false);
            }
        }
    }
}
