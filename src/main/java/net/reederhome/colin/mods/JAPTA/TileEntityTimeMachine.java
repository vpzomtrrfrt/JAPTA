package net.reederhome.colin.mods.JAPTA;

import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTimeMachine extends TileEntityJPT {

	int use = 200;
	int jump = 9;
	
	public void updateEntity() {
		super.updateEntity();
		int nm = 0;
		if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && amount >= use) {
			worldObj.setWorldTime(worldObj.getWorldTime()+jump);
			amount-=use;
			nm = 1;
		}
		if(!worldObj.isRemote) {
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, nm, 3);
		}
	};
	
	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return 10000;
	}

}
