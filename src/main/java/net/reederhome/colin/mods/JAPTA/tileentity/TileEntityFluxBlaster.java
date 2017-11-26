package net.reederhome.colin.mods.JAPTA.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.reederhome.colin.mods.JAPTA.JAPTA;
import net.reederhome.colin.mods.JAPTA.block.BlockBlaster;

public class TileEntityFluxBlaster extends TileEntityJPT implements TileEntityJPT.EnergyReceiver, TileEntityJPT.EnergyProvider, ITickable {
    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return 10000;
    }

    @Override
    public void update() {
        IBlockState state = getWorld().getBlockState(getPos());
        EnumFacing facing = JAPTA.safeGetValue(state, BlockBlaster.FACING);
        boolean inhaler = ((BlockBlaster) state.getBlock()).isInhaler();
        for (int i = 1; i <= BlockBlaster.RANGE; i++) {
            BlockPos cp = getPos().offset(facing, i);
            while (getWorld().getBlockState(cp).getBlock() == JAPTA.elevatorShaft) {
                cp = cp.up();
            }
            TileEntity te = getWorld().getTileEntity(cp);
            if (inhaler) {
                int remaining = getMaxEnergyStored(null) - stored;
                if (remaining > 0) {
                     stored += JAPTA.extractEnergy(te, facing.getOpposite(), remaining);
                }
            } else if (stored > 0) {
                stored -= JAPTA.receiveEnergy(te, facing.getOpposite(), stored);
            }
        }
        if (stored > 0 && inhaler) {
            transmit();
        }
    }

    @Override
    public boolean canTransmitEnergy(EnumFacing from) {
        return from != JAPTA.safeGetValue(this, BlockBlaster.FACING);
    }
}
