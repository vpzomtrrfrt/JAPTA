package net.reederhome.colin.mods.JAPTA;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFluxBlaster extends TileEntityJPT {

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 10000;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if(amount>0) {
			int range = 8;
			ForgeDirection side = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
			for(int i = 1; i <= range; i++) {
				int cx = xCoord+side.offsetX*i;
				int cy = yCoord+side.offsetY*i;
				int cz = zCoord+side.offsetZ*i;
				while(worldObj.getBlock(cx, cy, cz).equals(JAPTA.elevatorShaft)) {
					cy++;
				}
				TileEntity te = worldObj.getTileEntity(cx, cy, cz);
				if(te instanceof IEnergyReceiver) {
					IEnergyReceiver ie = (IEnergyReceiver) te;
					amount -= ie.receiveEnergy(side, amount, false);
				}
			}
		}
	}

}