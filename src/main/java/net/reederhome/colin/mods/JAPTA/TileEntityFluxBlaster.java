package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityFluxBlaster extends TileEntityJPT implements ITickable {

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return 10000;
	}
	
	public void update() {
		if(amount>0) {
			int range = 8;
			EnumFacing side = worldObj.getBlockState(getPos()).getValue(BlockFluxBlaster.FACING);
			for(int i = 1; i <= range; i++) {
				BlockPos cp = getPos().offset(side, i);
				while(worldObj.getBlockState(cp).getBlock().equals(JAPTA.elevatorShaft)) {
					cp = cp.up();
				}
				TileEntity te = worldObj.getTileEntity(cp);
				if(te instanceof IEnergyReceiver) {
					IEnergyReceiver ie = (IEnergyReceiver) te;
					amount -= ie.receiveEnergy(side, amount, false);
				}
			}
		}
	}

}