package net.reederhome.colin.mods.JAPTA;

import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTimeMachine extends TileEntityJPT {

	int use = 200;
	int jump = 9;
	
	public void updateEntity() {
		super.updateEntity();
		if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && amount >= use) {
			System.out.println(worldObj.getWorldTime());
			worldObj.setWorldTime(worldObj.getWorldTime()+jump);
			amount-=use;
		}
	};
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 10000;
	}

}
